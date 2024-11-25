package com.juandgaines.agenda.presentation.agenda_item

import com.juandgaines.agenda.domain.agenda.AgendaItemDetails
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.AttendeeUi
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.toAttendeeUi
import java.time.ZonedDateTime

sealed interface AgendaItemDetailsUi {

    data class EventDetails(
        val finishDate:ZonedDateTime = ZonedDateTime.now(),
        val host : String = "",
        val isUserCreator:Boolean = false,
        val attendees:List<AttendeeUi> = emptyList(),
    ): AgendaItemDetailsUi{
        val isGoing:List<AttendeeUi> = attendees.filter { it.isGoing }
        val isNotGoing:List<AttendeeUi> = attendees.filter { !it.isGoing }
    }

    data class TaskDetails(val isCompleted:Boolean = false): AgendaItemDetailsUi

    data object ReminderDetails: AgendaItemDetailsUi
}


fun AgendaItemDetails.toAgendaItemDetailsUi() = when(this){
    is AgendaItemDetails.EventDetails -> AgendaItemDetailsUi.EventDetails(
        finishDate = finishDate,
        host = host,
        isUserCreator = isUserCreator,
        attendees = attendees.map { it.toAttendeeUi() }
    )
    is AgendaItemDetails.TaskDetails -> AgendaItemDetailsUi.TaskDetails(isCompleted)
    is AgendaItemDetails.ReminderDetails -> AgendaItemDetailsUi.ReminderDetails
}