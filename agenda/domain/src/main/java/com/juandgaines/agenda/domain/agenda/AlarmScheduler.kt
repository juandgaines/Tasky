package com.juandgaines.agenda.domain.agenda

interface AlarmScheduler {
    fun scheduleAlarm( agendaItem: AgendaItems, clazz: Class<*>)
    fun cancelAlarm(agendaItem: AgendaItems, clazz: Class<*>)
}