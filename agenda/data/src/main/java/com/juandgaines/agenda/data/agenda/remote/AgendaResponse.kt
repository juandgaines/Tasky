package com.juandgaines.agenda.data.agenda.remote

import com.juandgaines.agenda.data.event.remote.EventResponse
import com.juandgaines.agenda.data.reminder.remote.ReminderResponse
import com.juandgaines.agenda.data.task.remote.TaskResponse
import kotlinx.serialization.Serializable

@Serializable
data class AgendaResponse(
    val tasks: List<TaskResponse>,
    val reminders:List<ReminderResponse>,
    val events: List<EventResponse>
)