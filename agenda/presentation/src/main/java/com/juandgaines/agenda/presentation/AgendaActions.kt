package com.juandgaines.agenda.presentation

import com.juandgaines.agenda.domain.agenda.AgendaItem.AgendaItems
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
    data class CreateAgendaItem(val agendaItems: AgendaItems) : AgendaActions
    data object Logout : AgendaActions
}