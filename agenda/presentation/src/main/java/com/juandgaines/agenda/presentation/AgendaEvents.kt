package com.juandgaines.agenda.presentation

import com.juandgaines.core.presentation.ui.UiText

sealed interface AgendaEvents {
    data object LogOut : AgendaEvents
    data class Error(val message: UiText) : AgendaEvents
}