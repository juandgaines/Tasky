package com.juandgaines.agenda.data.event

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.event.remote.EventApi
import com.juandgaines.agenda.data.event.remote.createEventRequestBody
import com.juandgaines.agenda.data.event.remote.updateEventRequestBody
import com.juandgaines.agenda.data.mappers.toEvent
import com.juandgaines.agenda.data.mappers.toEventEntity
import com.juandgaines.agenda.data.mappers.toEventRequest
import com.juandgaines.agenda.data.mappers.toUpdateEventRequest
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.event.EventRepository
import com.juandgaines.core.data.database.event.EventDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultEventRepository @Inject constructor(
    private val eventDao: EventDao,
    private val eventApi: EventApi,
    private val applicationScope: CoroutineScope,
    private val agendaItemScheduler: AgendaSyncScheduler
):EventRepository {
    override suspend fun insertEvent(event: Event): Result<Unit, DataError> {
        return try {
            val entity = event.toEventEntity()
            eventDao.upsertEvent(entity)

            val response = safeCall {
                val request = event.toEventRequest()
                val eventBody = createEventRequestBody(request)
                eventApi.createEvent(eventBody)
            }.onError {
                when (it) {
                    DataError.Network.SERVER_ERROR,
                    DataError.Network.REQUEST_TIMEOUT,
                    DataError.Network.NO_INTERNET ->{
                        agendaItemScheduler.scheduleSync(
                            AgendaSyncOperations.CreateAgendaItem(
                                event
                            )
                        )
                    }
                    else -> Unit
                }
            }.onSuccess {
                val newEvent = it.toEvent(null)
                eventDao.upsertEvent(newEvent.toEventEntity())
            }.asEmptyDataResult()
            response
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun updateEvent(event: Event): Result<Unit, DataError> {
        return try {
            val entity = event.toEventEntity()
            eventDao.upsertEvent(entity)

            val response = safeCall {
                val request = event.toUpdateEventRequest()
                val eventBody = updateEventRequestBody(request)
                eventApi.updateEvent(eventBody)
            }.onError {
                when (it) {
                    DataError.Network.SERVER_ERROR,
                    DataError.Network.REQUEST_TIMEOUT,
                    DataError.Network.NO_INTERNET ->{
                        agendaItemScheduler.scheduleSync(
                            AgendaSyncOperations.UpdateAgendaItem(
                                event
                            )
                        )
                    }
                    else -> Unit
                }
            }.asEmptyDataResult()
            response
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertEvents(list: List<Event>): Result<Unit, DataError> {
        return try {
            val entities = list.map { it.toEventEntity() }
            applicationScope.async {
                eventDao.upsertEvents(entities)
            }.await()
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun getEventById(eventId: String): Result<Event, DataError> {
        return eventDao.getEventById(eventId)?.toEvent()?.let {
            Result.Success(it)
        } ?: run {
            Result.Error(LocalError.NOT_FOUND)
        }
    }

    override fun getEventByIdFlow(eventId: String): Flow<Event?> {
        return eventDao.getEventByIdFlow(eventId).map {
            it?.toEvent()
        }
    }

    override suspend fun deleteEvent(event: Event): Result<Unit, DataError> {
        val reminderId = event.id
        eventDao.deleteEventById(reminderId)
        val response = safeCall {
            eventApi.deleteEventById(reminderId)
        }.onError {
            when (it) {
                DataError.Network.SERVER_ERROR,
                DataError.Network.REQUEST_TIMEOUT,
                DataError.Network.NO_INTERNET ->
                    agendaItemScheduler.scheduleSync(
                        AgendaSyncOperations.DeleteAgendaItem(
                            event
                        )
                    )
                else -> Unit
            }
        }.asEmptyDataResult()
        return response
    }

    override fun getEvents(
        startDate: Long,
        endDay: Long,
    ): Flow<List<Event>> {
        return eventDao.getEvents(
            startDate,
            endDay
        ).map { eventEntities->
            eventEntities.map {
                it.toEvent()
            }
        }
    }

    override suspend fun getEventsAfterDate(date: Long): List<Event> {
        return eventDao.getEventsAfterDate(date).map {
            it.toEvent()
        }
    }
}