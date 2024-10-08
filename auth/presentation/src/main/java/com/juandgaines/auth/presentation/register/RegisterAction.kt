package com.juandgaines.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePassWordVisibility : RegisterAction
    data object OnBackClick : RegisterAction
    data object OnRegisterClick : RegisterAction
}