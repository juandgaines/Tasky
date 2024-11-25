package com.juandgaines.agenda.presentation.agenda_item.components.attendee

import com.juandgaines.agenda.domain.agenda.InitialsCalculator.Companion.ONE_WORD

object UserInitialsFormatter {
    fun format(name: String = ""): String {
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
}