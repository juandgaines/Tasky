package com.juandgaines.agenda.presentation.agenda_item

import java.time.ZonedDateTime

data class AgendaItemState(
    val isEditing: Boolean = false,
    val isSelectTimeDialog: Boolean = false,
    val isSelectDateDialog: Boolean = false,
    val title: String = "",
    val description: String = "",
    val startDateTime: ZonedDateTime = ZonedDateTime.now(),
    val alarm: AlarmOptions = AlarmOptions.MINUTES_TEN,
    val details: AgendaItemDetails?  = null
)