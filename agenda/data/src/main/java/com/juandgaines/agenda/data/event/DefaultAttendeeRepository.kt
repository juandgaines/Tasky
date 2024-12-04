package com.juandgaines.agenda.data.event

import com.juandgaines.agenda.data.event.remote.AttendeeApi
import com.juandgaines.agenda.data.mappers.toMinimalAttendee
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.DeleteAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.agenda.AttendeeMinimal
import com.juandgaines.agenda.domain.event.AttendeeRepository
import com.juandgaines.core.data.database.event.EventDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.onError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultAttendeeRepository @Inject constructor(
    private val attendeeApi: AttendeeApi,
    private val eventDao: EventDao,
    private val agendaItemScheduler: AgendaSyncScheduler,
    private val applicationScope: CoroutineScope
):AttendeeRepository {

    override suspend fun getAttendeeByEmail(email: String): Result<AttendeeMinimal?, DataError> {
        val response = safeCall {
            attendeeApi.getAttendee(email)
        }.map {
            if (!it.doesUserExist) {
                null
            } else {
                it.attendee?.toMinimalAttendee()
            }
        }
        return response
    }

    override suspend fun deleteAttendee(agendaItem: AgendaItems): Result<Unit, DataError> = safeCall {
        val id = agendaItem.id
        eventDao.deleteEventById(id)

        return safeCall {
            attendeeApi.deleteAttendee(id)
        }.onError {
            when (it) {
                DataError.Network.SERVER_ERROR,
                DataError.Network.REQUEST_TIMEOUT,
                DataError.Network.NO_INTERNET ->
                    applicationScope.launch {
                        agendaItemScheduler.scheduleSync(
                            DeleteAgendaItem(
                                agendaItem = agendaItem
                            )
                        )
                    }
                else -> Unit
            }
        }.asEmptyDataResult()
    }
}