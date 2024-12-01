package com.juandgaines.auth.domain

import com.juandgaines.core.domain.auth.PatternValidator

class UserDataValidator (
    private val patternValidator: PatternValidator
) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.length >=4 && name.length <= 50

    }

    fun validatePassword(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
            hasNumber = password.any { it.isDigit() },
            hasLowerCaseCharacter = password.any { it.isLowerCase() },
            hasUpperCaseCharacter = password.any { it.isUpperCase() }
        )
    }

    companion object{
        const val MIN_PASSWORD_LENGTH = 9
    }
}