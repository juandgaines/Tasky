package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.util.Error
import java.time.ZonedDateTime

interface IAttendee{
    val email: String
    val fullName: String
    val userId: String
}

data class Attendee(
    override val email: String,
    override val fullName: String,
    override val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: ZonedDateTime,
    val isUserCreator: Boolean,
):IAttendee

data class AttendeeMinimal(
    override val email: String,
    override val fullName: String,
    override val userId: String,
):IAttendee

enum class ErrorAttendee:Error{
    USER_DOES_NOT_EXIST
}