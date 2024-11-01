package com.juandgaines.core.presentation.agenda

enum class AgendaItemOption {
    REMINDER,
    TASK,
    EVENT;
    companion object {
        fun fromOrdinal(ordinal: Int): AgendaItemOption {
            return entries.first { it.ordinal == ordinal }
        }
    }
}