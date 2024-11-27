package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.foundation.text.input.TextFieldState
import java.time.ZonedDateTime

data class AgendaItemState(
    val isEditing: Boolean = false,
    val isNew: Boolean = true,
    val isSelectTimeDialogVisible: Boolean = false,
    val isSelectDateDialog: Boolean = false,
    val description: String = "",
    val startDateTime: ZonedDateTime = ZonedDateTime.now(),
    val isAddAttendeeDialogVisible: Boolean = false,
    val alarm: AlarmOptions = AlarmOptions.MINUTES_TEN,
    val attendeeEmailBuffer: TextFieldState = TextFieldState(),
    val isAddingVisitor: Boolean = false,
    val isEmailError: Boolean = false,
    val details: AgendaItemDetailsUi = AgendaItemDetailsUi.ReminderDetails,
    val isEditingEndDate: Boolean = false,
    val title: String = "",
    val attendeeFilter: AttendeeFilter = AttendeeFilter.ALL,
)