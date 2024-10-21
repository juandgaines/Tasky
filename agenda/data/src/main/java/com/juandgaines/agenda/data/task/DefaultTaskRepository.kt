package com.juandgaines.agenda.data.task

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.data.mappers.toTaskEntity
import com.juandgaines.agenda.data.mappers.toTaskRequest
import com.juandgaines.agenda.data.task.remote.TaskApi
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Error
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
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
            }.onError {
                //TODO: Add to queue to create task later
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

            val response = safeCall {
                taskApi.updateTask(task.toTaskRequest())
            }.onError {
                agendaSyncScheduler.scheduleSync(
                    AgendaSyncOperations.UpdateAgendaItem(
                        task
                    )
                )
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

    override suspend fun deleteTask(task: Task): Result<Unit, DataError> {
        taskDao.deleteTask(task.id)
        val response = safeCall {
            taskApi.deleteTask(task.id)
        }.onError {
            applicationScope.launch {
                agendaSyncScheduler.scheduleSync(
                    AgendaSyncOperations.DeleteAgendaItem(
                        task
                    )
                )
            }.join()
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
}