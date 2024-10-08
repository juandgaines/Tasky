package com.juandgaines.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState

data class LoginState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isEmailValid: Boolean = false,
    val isError: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false
)
