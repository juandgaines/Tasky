package com.juandgaines.agenda.domain.agenda

interface AlarmScheduler {
    fun scheduleAlarm( agendaItem: AgendaItems)
    fun cancelAlarm(agendaItemId: String)
}