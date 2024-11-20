package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.auth.SessionManager

class InitialsCalculator(
    private val sessionManager: SessionManager
) {
    suspend fun getInitials(name: String = ""): String {
        if (name.isNotEmpty()) {
            return extractInitials(name)
        }
        else {
            val authData = sessionManager.get() ?: return ""
            return extractInitials(authData.fullName)
        }
    }

    fun getInitialsSync(name: String = ""): String {
        return if (name.isNotEmpty()) {
            extractInitials(name)
        }
        else {
            ""
        }
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