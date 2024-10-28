package com.juandgaines.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenNav {
    @Serializable
    data object AuthNav : ScreenNav
    @Serializable
    data object Register : ScreenNav
    @Serializable
    data object Login : ScreenNav
    @Serializable
    data object HomeNav : ScreenNav
    @Serializable
    data object Agenda : ScreenNav

    @Serializable
    data class AgendaItem(
        val id: String? = null,
        val type:Int,
        val isEditing:Boolean = false
    ) : ScreenNav
}