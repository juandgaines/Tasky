package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

sealed interface AgendaItemDetails {

    data class EventDetails(
        val finishDate:ZonedDateTime = ZonedDateTime.now(),
        val host : String = "",
        val isUserCreator:Boolean = false
    ): AgendaItemDetails

    data class TaskDetails(val isCompleted:Boolean = false): AgendaItemDetails

    data object ReminderDetails: AgendaItemDetails
}