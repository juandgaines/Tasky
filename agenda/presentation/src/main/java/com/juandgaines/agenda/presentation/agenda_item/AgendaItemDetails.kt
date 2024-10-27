package com.juandgaines.agenda.presentation.agenda_item

import java.time.ZonedDateTime

sealed interface AgendaItemDetails {

    data class EventDetails(
        val finishDate:ZonedDateTime
    ): AgendaItemDetails

    data object TaskDetails: AgendaItemDetails

    data object ReminderDetails: AgendaItemDetails
}