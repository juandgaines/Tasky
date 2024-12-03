package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

sealed interface AgendaItemDetails {

    data class EventDetails(
        val finishDate:ZonedDateTime = ZonedDateTime.now(),
        val host : String = "",
        val isUserCreator:Boolean = false,
        val isGoingUser : Boolean,
        val attendees:List<IAttendee> = emptyList(),
    ): AgendaItemDetails{
        val isGoing:List<IAttendee> = attendees.filter { it.isGoing }
        val isNotGoing:List<IAttendee> = attendees.filter { !it.isGoing }
    }

    data class TaskDetails(val isCompleted:Boolean = false): AgendaItemDetails

    data object ReminderDetails: AgendaItemDetails
}