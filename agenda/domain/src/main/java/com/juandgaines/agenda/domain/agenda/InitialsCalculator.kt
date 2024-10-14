package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.auth.SessionManager

class InitialsCalculator(
    private val sessionManager: SessionManager
) {
    suspend fun getInitials(): String {
        val authData = sessionManager.get() ?: return "" // Return empty if no auth data

        return extractInitials(authData.fullName)
    }

    private fun extractInitials(fullName: String): String {
        val nameParts =fullName.trim().split("\\s+".toRegex())

        return when (nameParts.size) {
            ONE_WORD -> nameParts.first().take(2).uppercase()
            else -> {
                val firstInitial = nameParts.first().first().uppercaseChar()
                val lastInitial = nameParts.last().first().uppercaseChar()
                "$firstInitial$lastInitial"
            }
        }
    }

    companion object {
        const val ONE_WORD = 1
    }
}