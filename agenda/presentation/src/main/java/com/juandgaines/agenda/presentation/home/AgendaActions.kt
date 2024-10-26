package com.juandgaines.agenda.presentation.home

import com.juandgaines.agenda.domain.task.Task
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
    data object Logout : AgendaActions
}