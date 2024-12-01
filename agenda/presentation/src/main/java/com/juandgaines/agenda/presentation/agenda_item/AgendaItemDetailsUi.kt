package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import com.juandgaines.agenda.domain.agenda.AgendaItemDetails
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.AttendeeUi
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.toAttendeeUi
import com.juandgaines.core.domain.auth.PatternValidator
import java.time.ZonedDateTime

sealed interface AgendaItemDetailsUi {

    data class EventDetails(
        val finishDate:ZonedDateTime = ZonedDateTime.now(),
        val host : String = "",
        val isUserCreator:Boolean = false,
        val attendees:List<AttendeeUi> = emptyList(),
        val attendeeEmailBuffer: TextFieldState = TextFieldState(),
        val isAddingVisitor: Boolean = false,
        val doesEmailExist: Boolean = false,
        val isAddAttendeeDialogVisible: Boolean = false,
        val isConnectedToInternet: Boolean = true,
        private val emailPatterValidator: PatternValidator
    ): AgendaItemDetailsUi{
        val isGoing:List<AttendeeUi> = attendees.filter { it.isGoing }
        val isNotGoing:List<AttendeeUi> = attendees.filter { !it.isGoing }
        val isEmailValid = snapshotFlow {
            attendeeEmailBuffer.text.toString().let { emailPatterValidator.matches(it) }
        }
    }

    data class TaskDetails(val isCompleted:Boolean = false): AgendaItemDetailsUi

    data object ReminderDetails: AgendaItemDetailsUi
}


fun AgendaItemDetails.toAgendaItemDetailsUi(emailPatterValidator: PatternValidator) = when(this){
    is AgendaItemDetails.EventDetails -> AgendaItemDetailsUi.EventDetails(
        finishDate = finishDate,
        host = host,
        isUserCreator = isUserCreator,
        attendees = attendees.map {
            it.toAttendeeUi()
        },
        emailPatterValidator = emailPatterValidator
    )
    is AgendaItemDetails.TaskDetails -> AgendaItemDetailsUi.TaskDetails(isCompleted)
    is AgendaItemDetails.ReminderDetails -> AgendaItemDetailsUi.ReminderDetails
}