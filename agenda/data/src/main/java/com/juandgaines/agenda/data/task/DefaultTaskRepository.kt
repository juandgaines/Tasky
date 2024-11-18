package com.juandgaines.agenda.data.task

import android.database.sqlite.SQLiteException
import android.util.Log
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.data.mappers.toTaskEntity
import com.juandgaines.agenda.data.mappers.toTaskRequest
import com.juandgaines.agenda.data.task.remote.TaskApi
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.DataError.Network.NO_INTERNET
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.onError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApi: TaskApi,
    private val applicationScope: CoroutineScope,
    private val agendaSyncScheduler: AgendaSyncScheduler,
): TaskRepository {
    override suspend fun insertTask(task: Task): Result<Unit, DataError> {
        return try {
            val entity = task.toTaskEntity()
            taskDao.upsertTask(entity)
            val response = safeCall {
                taskApi.createTask(task.toTaskRequest())
            }.onError { error->
               when (error){
                   DataError.Network.SERVER_ERROR,
                   DataError.Network.REQUEST_TIMEOUT,
                   DataError.Network.NO_INTERNET ->{
                       agendaSyncScheduler.scheduleSync(
                            AgendaSyncOperations.CreateAgendaItem(
                                 task
                            )
                          )
                   }
                   else-> Unit
               }
            }.asEmptyDataResult()
            return response
        } catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit, DataError> {
        return try {
            val entity = task.toTaskEntity()
            taskDao.upsertTask(entity)
            Log.d("Repository Update", "updateReminder: $task")
            val response = safeCall {
                taskApi.updateTask(task.toTaskRequest())
            }.onError {
                when (it) {
                    DataError.Network.SERVER_ERROR,
                    DataError.Network.REQUEST_TIMEOUT,
                    DataError.Network.NO_INTERNET ->{
                        agendaSyncScheduler.scheduleSync(
                            AgendaSyncOperations.UpdateAgendaItem(
                                task
                            )
                        )
                    }
                    else -> Unit
                }
            }.asEmptyDataResult()

            return response
        } catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertTasks(list: List<Task>): Result<Unit, DataError> {
        return try {
            val entities = list.map { it.toTaskEntity()}
            applicationScope.async {
                taskDao.upsertTasks(entities)
            }.await()

            Result.Success(Unit)
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun getTaskById(taskId: String): Result<Task, DataError> {
        return taskDao.getTaskById(taskId)?.toTask()?.let {
            Result.Success(it)
        } ?: run {
            Result.Error(LocalError.NOT_FOUND)
        }
    }

    override fun getTaskByIdFlow(taskId: String): Flow<Task?> {
        return taskDao.getTaskByIdFlow(taskId).map {
            it?.toTask()
        }
    }

    override suspend fun deleteTask(task: Task): Result<Unit, DataError> {
        taskDao.deleteTask(task.id)
        val response = safeCall {
            taskApi.deleteTask(task.id)
        }.onError {
            when (it) {
                NO_INTERNET -> {
                    applicationScope.launch {
                        agendaSyncScheduler.scheduleSync(
                            AgendaSyncOperations.DeleteAgendaItem(
                                task
                            )
                        )
                    }.join()
                }
                else -> Unit
            }

        }.asEmptyDataResult()

        return response
    }

    override fun getTasks(
        startDate: Long,
        endDay: Long
    ): Flow<List<Task>> {
        return taskDao
            .getTasks(
                startDate,
                endDay
            )
            .map { taskEntities ->
                taskEntities.map {
                    it.toTask()
                }
            }
    }

    override suspend fun getTasksAfterDate(date: Long): List<Task> {
        return taskDao.getTasksAfterDate(date)
            .map {
                it.toTask()
            }
    }
}