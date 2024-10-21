package com.juandgaines.agenda.data.agenda

import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.data.mappers.toTaskRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderApi
import com.juandgaines.agenda.data.task.remote.TaskApi
import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.EmptyDataResult
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAgendaRepository @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository,
    private val sessionManager: SessionManager,
    private val agendaApi: AgendaApi,
    private val agendaSyncDao: AgendaSyncDao,
    private val taskApi: TaskApi,
    private val reminderApi: ReminderApi,
    private val applicationScope: CoroutineScope
) : AgendaRepository {

    override fun getItems(
        startDate: Long,
        endDay: Long
    ): Flow<List<AgendaItem>> = combine(
        reminderRepository.getReminders(
            startDate,
            endDay
        ),
        taskRepository.getTasks(
            startDate,
            endDay
        )
    ){ reminders, tasks ->
        (reminders + tasks)
    }

    override suspend fun fetchItems(time: Long): EmptyDataResult<Network> = safeCall {
        agendaApi.getAgenda(time)
    }.onSuccess { response->
        val taskItems= response.tasks.map { it.toTask() }
        val reminderItems= response.reminders.map { it.toReminder() }
        applicationScope.launch {
            launch {
                reminderRepository.upsertReminders(reminderItems)
            }
            launch {
                taskRepository.upsertTasks(taskItems)
            }
        }
    }.asEmptyDataResult()

    override suspend fun syncPendingAgendaItem() {
        return withContext(Dispatchers.IO){
            val userId = sessionManager.get()?.userId ?: return@withContext

            val pendingDeleteReminders = async {
                agendaSyncDao.getAllDeleteReminderSync(userId)
            }

            val pendingDeleteTasks = async{
                agendaSyncDao.getAllDeleteTaskSync(userId)
            }
            val pendingUpdatedTasks = async{
                agendaSyncDao.getAllUpdateTaskSync(userId)
            }

            val pendingDeleteJobs = pendingDeleteTasks
                .await()
                .map {
                    launch {
                        val taskId: String = it.taskId
                        safeCall {
                                taskApi.deleteTask(taskId)
                         }.onSuccess {
                             applicationScope.launch {
                                 agendaSyncDao.deleteDeleteTaskSync(taskId)
                             }.join()
                         }
                    }
                }
            val pendingUpdateJobs = pendingUpdatedTasks
                .await()
                .map {
                    launch {
                        val task = it.task.toTask()
                        safeCall {
                            taskApi.updateTask(task.toTaskRequest())
                        }.onSuccess {
                            applicationScope.launch {
                                agendaSyncDao.deleteUpdateTaskSync(task.id)
                            }.join()
                        }
                    }
                }

            val pendingDeleteReminderJobs = pendingDeleteReminders
                .await()
                .map {
                    launch {
                        val reminderId = it.reminderId
                        safeCall {
                            reminderApi.deleteReminderById(reminderId)
                        }.onSuccess {
                            applicationScope.launch {
                                agendaSyncDao.deleteDeleteReminderSync(reminderId)
                            }.join()
                        }
                    }
                }

            pendingUpdateJobs.forEach {
                it.join()
            }
            pendingDeleteJobs.forEach {
                it.join()
            }
            pendingDeleteReminderJobs.forEach {
                it.join()
            }
            Result.Success(Unit)
        }
    }
}