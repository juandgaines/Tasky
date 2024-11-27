package com.juandgaines.agenda.data.event

import com.juandgaines.agenda.data.event.remote.AttendeeApi
import com.juandgaines.agenda.data.mappers.toMinimalAttendee
import com.juandgaines.agenda.domain.agenda.AttendeeMinimal
import com.juandgaines.agenda.domain.event.AttendeeRepository
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.map
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DefaultAttendeeRepository @Inject constructor(
    private val attendeeApi: AttendeeApi,
    private val applicationScope: CoroutineScope,
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

    override suspend fun deleteAttendee(eventId: String): Result<Unit, DataError> = safeCall {
        attendeeApi.deleteAttendee(eventId)
    }
}