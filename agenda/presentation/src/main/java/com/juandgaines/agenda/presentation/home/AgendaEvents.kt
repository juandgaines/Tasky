package com.juandgaines.agenda.presentation.home

import com.juandgaines.core.presentation.agenda.AgendaItemOption
import com.juandgaines.core.presentation.ui.UiText

sealed interface AgendaEvents {
    data object LogOut : AgendaEvents
    data class Success(val message: UiText) : AgendaEvents
    data class Error(val message: UiText) : AgendaEvents
    data class GoToItemScreen(
        val id:String? = null,
        val type: AgendaItemOption,
        val isEditing:Boolean,
        val dateEpochMilli:Long? = null
    ) : AgendaEvents
}