package com.juandgaines.agenda.presentation.agenda_item

import java.time.ZonedDateTime

data class AgendaItemState(
    val isEditing: Boolean = true,
    val title: String = "",
    val description: String = "",
    val startDateTime: ZonedDateTime,
    val details: AgendaItemDetails
)