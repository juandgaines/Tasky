package com.juandgaines.core.presentation.navigation

import com.juandgaines.core.domain.agenda.AgendaItemOption
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
        val type: AgendaItemOption,
        val isEditing:Boolean = false,
        val dateEpochMillis:Long? = null
    ) : ScreenNav

    @Serializable
    data class EditField(
        val fieldName:String,
        val fieldValue:String
    ):ScreenNav
}