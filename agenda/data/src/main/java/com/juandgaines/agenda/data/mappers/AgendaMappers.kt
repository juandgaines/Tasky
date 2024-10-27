package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.agenda.remote.AgendaResponse
import com.juandgaines.agenda.domain.agenda.AgendaItems

fun AgendaResponse.mapToListAgenda(): List<AgendaItems> {
    return this.tasks.map {
        it.toTask()
    } + this.reminders.map {
        it.toReminder()
    }
}