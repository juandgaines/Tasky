package com.juandgaines.agenda.presentation.agenda_item.components.attendee

import com.juandgaines.agenda.domain.agenda.Attendee

sealed interface AttendeeUI{
    data class Going(
        val goingAttendee:List<Attendee>
    )
    data class NotGoing(
        val notGoingAttendee:List<Attendee>
    )
}