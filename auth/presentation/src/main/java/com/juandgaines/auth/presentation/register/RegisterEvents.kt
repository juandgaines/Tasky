package com.juandgaines.auth.presentation.register

import com.juandgaines.core.presentation.ui.UiText

sealed interface RegisterEvents {
    data object RegistrationSuccess : RegisterEvents
    data class Error(val error: UiText) : RegisterEvents
}