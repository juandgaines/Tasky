package com.juandgaines.agenda.presentation.agenda_item

import com.juandgaines.agenda.domain.agenda.AlarmOptions
import java.time.ZonedDateTime

data class AgendaItemState(
    val isEditing: Boolean = true,
    val title: String = "",
    val description: String = "",
    val startDateTime: ZonedDateTime,
    val alarm: AlarmOptions,
    val details: AgendaItemDetails
)