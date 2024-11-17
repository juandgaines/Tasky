package com.juandgaines.agenda.presentation.agenda_item

import java.time.ZonedDateTime

sealed interface AgendaItemDetails {

    data class EventDetails(
        val finishDate:ZonedDateTime = ZonedDateTime.now(),
        val isEditingFinishDate:Boolean = false,
        val host : String = "",
        val isUserCreator:Boolean = false
    ): AgendaItemDetails

    data class TaskDetails(val isCompleted:Boolean = false): AgendaItemDetails

    data object ReminderDetails: AgendaItemDetails
}