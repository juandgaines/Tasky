package com.juandgaines.agenda.domain.event

import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun insertEvent(event: Event): Result<Unit, DataError>
    suspend fun updateEvent(event: Event): Result<Unit, DataError>
    suspend fun upsertEvents(list: List<Event>): Result<Unit, DataError>
    suspend fun getEventById(eventId: String): Result<Event, DataError>
    fun getEventByIdFlow(eventId: String): Flow<Event?>
    suspend fun deleteEvent(event: Event): Result<Unit, DataError>
    fun getEvents(
        startDate: Long,
        endDay: Long
    ): Flow<List<Event>>

    suspend fun getEventsAfterDate(
        date: Long
    ): List<Event>
}