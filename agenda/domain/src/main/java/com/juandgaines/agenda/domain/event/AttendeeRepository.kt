package com.juandgaines.agenda.domain.event

import com.juandgaines.agenda.domain.agenda.AttendeeMinimal
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result

interface AttendeeRepository {
    suspend fun getAttendeeByEmail(email: String): Result<AttendeeMinimal?, DataError>
    suspend fun deleteAttendee(eventId: String): Result<Unit, DataError>
}