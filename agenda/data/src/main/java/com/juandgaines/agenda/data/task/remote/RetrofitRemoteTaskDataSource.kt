package com.juandgaines.agenda.data.task.remote

import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.task.RemoteTaskDataSource
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.map
import javax.inject.Inject

class RetrofitRemoteTaskDataSource @Inject constructor(
    private val taskApi: TaskApi
):RemoteTaskDataSource{
    override suspend fun getTaskById(taskId: String): Result<Task, Network>{
        return safeCall {
            taskApi.getTaskById(taskId)
        }.map { taskResponse->
            taskResponse.toTask()
        }
    }
}