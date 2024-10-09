package com.juandgaines.tasky.navigation

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
}