package com.juandgaines.agenda.domain.task

import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface LocalTaskDataSource {
    suspend fun upsertTask(task:Task):Result<String,LocalError>
    suspend fun upsertTasks(tasks:List<Task>):Result<List<String>,LocalError>
    suspend fun getTaskById(taskId:String): Result<Task,LocalError>
    fun getTasks():Flow<List<Task>>
}