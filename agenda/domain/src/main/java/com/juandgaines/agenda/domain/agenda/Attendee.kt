package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

data class Attendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: ZonedDateTime,
    val isUserCreator: Boolean,
    val initials: String
)