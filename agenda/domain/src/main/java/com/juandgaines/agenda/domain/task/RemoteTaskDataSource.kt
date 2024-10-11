package com.juandgaines.agenda.domain.task

import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result

interface RemoteTaskDataSource {
    suspend fun getTaskById(taskId:String): Result<Task,Network>
}