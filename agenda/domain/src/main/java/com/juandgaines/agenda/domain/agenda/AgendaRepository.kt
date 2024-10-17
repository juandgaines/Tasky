package com.juandgaines.agenda.domain.agenda

import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface AgendaRepository{
    fun getItems(
        startDate:Long,
        endDay:Long
    ): Flow<List<AgendaItem>>

    suspend fun fetchItems(
        time:Long
    ): EmptyDataResult<Network>

    suspend fun updateTask(
        taskId: String,
    ): EmptyDataResult<DataError>
}