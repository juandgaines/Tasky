package com.juandgaines.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.juandgaines.auth.domain.PasswordValidationState

data class RegisterState(
    val fullName: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val canRegister: Boolean = false,
    val isRegistering: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isEmailValid: Boolean = false,
    val isNameValid: Boolean = false,
    val passWordValidationState: PasswordValidationState = PasswordValidationState()
)
