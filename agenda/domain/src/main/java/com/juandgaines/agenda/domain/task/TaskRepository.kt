package com.juandgaines.agenda.domain.task

import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task): Result<Unit, DataError>
    suspend fun updateTask(task: Task): Result<Unit, DataError>
    suspend fun upsertTasks(list: List<Task>): Result<Unit, DataError>
    suspend fun getTaskById(taskId: String): Result<Task, DataError>
    suspend fun deleteTask(task: Task):Result<Unit,DataError>
    fun getTasks(
        startDate: Long,
        endDay: Long
    ): Flow<List<Task>>
}