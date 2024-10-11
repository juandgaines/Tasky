package com.juandgaines.agenda.data.agenda.remote

import com.juandgaines.agenda.data.task.remote.TaskResponse
import kotlinx.serialization.Serializable

@Serializable
data class AgendaResponse(
    val tasks: List<TaskResponse>
)