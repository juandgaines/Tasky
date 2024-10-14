package com.juandgaines.agenda.domain.agenda

interface AgendaItem{
    val date:Long

    sealed interface AgendaItems{
        data object Task: AgendaItems
        data object Reminder: AgendaItems

        companion object{
            val entries  = listOf(Task, Reminder)
        }
    }
}



