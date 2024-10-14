package com.juandgaines.agenda.domain.agenda

sealed interface AgendaItems{
    data object Task: AgendaItems
    data object Reminder: AgendaItems

    companion object{
        val entries  = listOf(Task, Reminder)
    }
}

