package com.juandgaines.agenda.presentation.agenda_item.components.attendee

import com.juandgaines.agenda.domain.agenda.Attendee
import com.juandgaines.agenda.domain.agenda.IAttendee

data class AttendeeUi(
    val initials: String,
    val email: String,
    val fullName: String,
    val isCreator: Boolean,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val isUserCreator: Boolean,
)

fun Attendee.toAttendeeUi() = AttendeeUi(
    email = email,
    fullName = fullName,
    userId = userId,
    eventId = eventId,
    isGoing = isGoing,
    isUserCreator = isUserCreator,
    initials = UserInitialsFormatter.format(fullName),
    isCreator = isUserCreator
)

fun IAttendee.toAttendeeUi() = AttendeeUi(
    email = email,
    fullName = fullName,
    userId = userId,
    eventId = "",
    isGoing = isGoing,
    isUserCreator = isUserCreator,
    initials = UserInitialsFormatter.format(fullName),
    isCreator = isUserCreator
)