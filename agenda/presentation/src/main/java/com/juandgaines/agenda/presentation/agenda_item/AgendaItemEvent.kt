package com.juandgaines.agenda.presentation.agenda_item

import com.juandgaines.core.presentation.ui.UiText

sealed interface AgendaItemEvent{
    data object Created: AgendaItemEvent
    data object Updated: AgendaItemEvent
    data object Deleted: AgendaItemEvent
    data object CreationScheduled: AgendaItemEvent
    data object UpdateScheduled: AgendaItemEvent
    data object DeletionScheduled: AgendaItemEvent
    data class Error(val uiText: UiText): AgendaItemEvent
}