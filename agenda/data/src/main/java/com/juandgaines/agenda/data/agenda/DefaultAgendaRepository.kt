package com.juandgaines.agenda.data.agenda

import com.juandgaines.agenda.data.R
import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.EmptyDataResult
import com.juandgaines.core.domain.util.Error
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultAgendaRepository @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository,
    private val agendaApi: AgendaApi,
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

    override suspend fun updateTask(taskId:String): EmptyDataResult<DataError> {
        val response= taskRepository.getTaskById(taskId)
            .onSuccess {
                taskRepository.updateTask(it.copy(isDone = !it.isDone))
            }
        return response.asEmptyDataResult()
    }
}