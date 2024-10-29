package com.juandgaines.agenda.presentation.agenda_item

import java.time.ZonedDateTime

data class AgendaItemState(
    val isEditing: Boolean = false,
    val isSelectTimeDialog: Boolean = false,
    val isSelectDateDialog: Boolean = false,
    val description: String = "",
    val startDateTime: ZonedDateTime = ZonedDateTime.now(),
    val alarm: AlarmOptions = AlarmOptions.MINUTES_TEN,
    val details: AgendaItemDetails?  = null,
    val title: String = when (details) {
        is AgendaItemDetails.ReminderDetails -> "Reminder"
        is AgendaItemDetails.EventDetails -> "Event"
        is AgendaItemDetails.TaskDetails -> "Task"
        else -> "Meeting"
    },
)