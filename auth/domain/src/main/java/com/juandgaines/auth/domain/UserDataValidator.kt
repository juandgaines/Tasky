package com.juandgaines.auth.domain

class UserDataValidator (
    private val patternValidator: PatternValidator
) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }
}