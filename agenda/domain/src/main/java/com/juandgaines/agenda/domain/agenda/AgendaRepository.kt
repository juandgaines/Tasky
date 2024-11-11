package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.EmptyDataResult
import kotlinx.coroutines.flow.Flow

interface AgendaRepository{
    fun getItems(
        startDate:Long,
        endDay:Long
    ): Flow<List<AgendaItems>>

    suspend fun fetchItems(
        time:Long
    ): EmptyDataResult<Network>

    suspend fun fetchFutureItems(
        time:Long
    ): List<AgendaItems>

    suspend fun syncPendingAgendaItem()
}