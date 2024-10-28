package com.juandgaines.agenda.presentation.home

import com.juandgaines.core.presentation.ui.UiText

sealed interface AgendaEvents {
    data object LogOut : AgendaEvents
    data class Success(val message: UiText) : AgendaEvents
    data class Error(val message: UiText) : AgendaEvents
    data class GoToDetail(val id:String, val type: AgendaItemOption, val isEditing:Boolean) : AgendaEvents
}