package com.juandgaines.agenda.domain.agenda

import kotlinx.coroutines.flow.Flow

interface AgendaRepository{
    fun getItems(date:Long): Flow<List<AgendaItem>>
}