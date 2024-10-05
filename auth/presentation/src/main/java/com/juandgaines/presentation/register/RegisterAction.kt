package com.juandgaines.presentation.register

sealed interface RegisterAction {
    data object OnTogglePassWordVisibilityClick : RegisterAction
    data object OnBackClick : RegisterAction
    data object OnRegisterClick : RegisterAction
}