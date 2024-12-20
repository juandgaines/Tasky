package com.juandgaines.agenda.presentation.home

import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.core.domain.agenda.AgendaItemOption
import java.time.LocalDate

sealed interface AgendaActions{
    data object ShowDateDialog : AgendaActions
    data object DismissDateDialog : AgendaActions
    data object ShowProfileMenu: AgendaActions
    data object DismissProfileMenu: AgendaActions
    data class SelectDate(val date: Long) : AgendaActions
    data class SelectDateWithingRange(val date: LocalDate) : AgendaActions
    data object ShowCreateContextMenu : AgendaActions
    data object DismissCreateContextMenu : AgendaActions
    data class CreateItem(val option: AgendaItemOption) : AgendaActions
    data class AgendaOperation(val agendaOperation: AgendaCardMenuOperations) : AgendaActions
    data class ToggleDoneTask(val task: Task) : AgendaActions
    data class SendNotificationPermission(
        val permission: Boolean,
        val needRationale: Boolean
    ) : AgendaActions
    data class SendScheduleAlarmPermission(
        val permission: Boolean,
    ):AgendaActions
    data object Logout : AgendaActions
}