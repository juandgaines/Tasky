package com.juandgaines.agenda.domain.task

import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result

interface RemoteTaskDataSource {
    suspend fun createTask(task:Task): Result<Unit,Network>
    suspend fun updateTask(task: Task): Result<Unit,Network>
    suspend fun getTaskById(taskId:String): Result<Task,Network>
    suspend fun deleteTask(taskId:String): Result<Unit,Network>
}