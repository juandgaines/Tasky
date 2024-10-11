package com.juandgaines.agenda.data.task.remote

import com.juandgaines.agenda.domain.task.RemoteTaskDataSource
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import javax.inject.Inject

class RetrofitRemoteTaskDataSource @Inject constructor(

):RemoteTaskDataSource{
    override suspend fun createTask(task: Task): Result<Unit, Network> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task): Result<Unit, Network> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: String): Result<Task, Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String): Result<Unit, Network> {
        TODO("Not yet implemented")
    }
}