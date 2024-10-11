package com.juandgaines.agenda.data.task.local

import com.juandgaines.agenda.domain.task.LocalTaskDataSource
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalTaskDataSource @Inject constructor(
    private val taskDao: TaskDao
): LocalTaskDataSource{
    override suspend fun upsertTask(task: Task): Result<String, LocalError> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertTasks(tasks: List<Task>): Result<List<String>, LocalError> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: String): Result<Task, LocalError> {
        TODO("Not yet implemented")
    }

    override fun getTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }
}