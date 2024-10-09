package com.juandgaines.core.domain.navigation


sealed interface Screens{
    sealed interface Auth: Screens{
        data object Login: Auth
        data object Register: Auth
    }
    sealed interface Home: Screens{
        data object Agenda: Screens
    }
}