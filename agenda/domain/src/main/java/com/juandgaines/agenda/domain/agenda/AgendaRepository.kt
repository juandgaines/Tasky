package com.juandgaines.agenda.domain.agenda

import kotlinx.coroutines.flow.Flow

interface AgendaRepository{
    fun getItems(
        startDate:Long,
        endDay:Long
    ): Flow<List<AgendaItem>>
}