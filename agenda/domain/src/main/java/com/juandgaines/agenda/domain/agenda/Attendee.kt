package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

interface IAttendee{
    val email: String
    val fullName: String
    val userId: String
    val isGoing: Boolean
    val isUserCreator: Boolean
}

data class Attendee(
    override val email: String,
    override val fullName: String,
    override val userId: String,
    val eventId: String,
    override val isGoing: Boolean,
    val remindAt: ZonedDateTime,
    override val isUserCreator: Boolean,
):IAttendee

data class AttendeeMinimal(
    override val email: String,
    override val fullName: String,
    override val userId: String,
    override val isGoing: Boolean,
    override val isUserCreator: Boolean,
):IAttendee
