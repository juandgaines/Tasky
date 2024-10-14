package com.juandgaines.agenda.domain.agenda

sealed interface AgendaItems{
    data object Task: AgendaItems
    data object Reminder: AgendaItems
}

val agendaItems  = listOf(AgendaItems.Task, AgendaItems.Reminder)