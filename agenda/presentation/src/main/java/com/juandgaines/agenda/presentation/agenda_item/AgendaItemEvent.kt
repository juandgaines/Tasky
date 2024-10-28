package com.juandgaines.agenda.presentation.agenda_item

sealed interface AgendaItemEvent{
    data object Saved: AgendaItemEvent
}