package com.juandgaines.agenda.domain.task
import com.juandgaines.core.domain.util.Error
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task): Result<Unit, Error>
    suspend fun updateTask(task: Task): Result<Unit, Error>
    suspend fun upsertTasks(list: List<Task>): Result<Unit, Error>
    suspend fun getTaskById(taskId: String): Result<Task, Error>
    suspend fun deleteTask(taskId: String):Result<Unit,Error>
    fun getTasks(): Flow<List<Task>>
}