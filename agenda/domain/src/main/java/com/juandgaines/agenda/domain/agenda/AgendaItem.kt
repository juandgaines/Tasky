package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

interface AgendaItem{
    val id : String
    val type: AgendaType
    val date:ZonedDateTime
}

sealed interface AgendaType{
    data object Task: AgendaType
    data object Event: AgendaType
    data object Reminder: AgendaType
}


