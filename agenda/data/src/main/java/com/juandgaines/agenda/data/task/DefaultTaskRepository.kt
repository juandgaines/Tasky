package com.juandgaines.agenda.data.task

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.data.mappers.toTaskEntity
import com.juandgaines.agenda.data.mappers.toTaskRequest
import com.juandgaines.agenda.data.task.remote.TaskApi
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Error
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApi: TaskApi,
    private val applicationScope: CoroutineScope
): TaskRepository {
    override suspend fun insertTask(task: Task): Result<Unit, Error> {
        return try {
            val entity = task.toTaskEntity()
            taskDao.upsertTask(entity)
            val response = safeCall {
                taskApi.createTask(task.toTaskRequest())
            }
            when(response) {
                is Result.Success -> {
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    //TODO: Add to queue to create task later
                    Result.Error(response.error)
                }
            }
        } catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit, Error> {
        return try {
            val entity = task.toTaskEntity()
            taskDao.upsertTask(entity)

            val response = safeCall {
                taskApi.updateTask(task.toTaskRequest())
            }
            when(response) {
                is Result.Success -> {
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    //TODO: Add to queue to update task later
                    Result.Error(response.error)
                }
            }
        } catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertTasks(list: List<Task>): Result<Unit, Error> {
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

    override suspend fun getTaskById(taskId: String): Result<Task, Error> {
            val task = taskDao.getTaskById(taskId)?.toTask()
            return task?.let {
                val response = safeCall {
                    taskApi.getTaskById(taskId)
                }.map { taskResponse->
                    taskResponse.toTask()
                }
                when(response){
                    is Result.Error ->{
                        //TODO: Add to queue to get task later
                    }
                    is Result.Success -> {
                        applicationScope.async {
                            response.map { task->
                                taskDao.upsertTask(task.toTaskEntity())
                            }
                        }.await()
                    }
                }
                return Result.Success(task)
            }?:Result.Error(LocalError.NOT_FOUND)

    }

    override suspend fun deleteTask(taskId: String): Result<Unit, Error> {
        taskDao.deleteTask(taskId)
        val response = safeCall {
            taskApi.deleteTask(taskId)
        }
        return when(response) {
            is Result.Error -> {
                //TODO: Add to queue to delete task later
                Result.Error(response.error)
            }

            is Result.Success -> {
                Result.Success(Unit)
            }
        }
    }

    override fun getTasks(time:Long): Flow<List<Task>> {
        return taskDao
            .getTasks(time)
            .map { taskEntities ->
                taskEntities.map {
                    it.toTask()
                }
            }
    }
}