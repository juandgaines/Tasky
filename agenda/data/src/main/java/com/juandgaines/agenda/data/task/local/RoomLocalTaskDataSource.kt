package com.juandgaines.agenda.data.task.local

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.data.mappers.toTaskEntity
import com.juandgaines.agenda.domain.task.LocalTaskDataSource
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalTaskDataSource @Inject constructor(
    private val taskDao: TaskDao
): LocalTaskDataSource{
    override suspend fun upsertTask(task: Task): Result<String, LocalError> {
        return try{
            taskDao.upsertTask(task.toTaskEntity())
             Result.Success(task.id)
        }catch (e:SQLiteException){
            return Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertTasks(tasks: List<Task>): Result<List<String>, LocalError> {
        return try{
            val entities = tasks.map { it.toTaskEntity() }
            taskDao.upsertTasks(entities)
            Result.Success(entities.map { it.id })
        }catch (e:SQLiteException){
            return Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun getTaskById(taskId: String): Result<Task, LocalError> {
        val taskEntity = taskDao.getTaskById(taskId)
        return taskEntity?.let {
            Result.Success(taskEntity.toTask())
        }?: Result.Error(LocalError.NOT_FOUND)
    }

    override fun getTasks(): Flow<List<Task>> {
        return taskDao
            .getTasks()
            .map { taskEntities ->
                taskEntities.map {
                    it.toTask()
                }
            }
    }
}