package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.agenda.remote.AgendaResponse
import com.juandgaines.agenda.domain.agenda.AgendaItem

fun AgendaResponse.mapToListAgenda(): List<AgendaItem> {
    return this.tasks.map {
        it.toTask()
    } + this.reminders.map {
        it.toReminder()
    }
}