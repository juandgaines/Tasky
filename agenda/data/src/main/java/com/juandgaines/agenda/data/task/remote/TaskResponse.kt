package com.juandgaines.agenda.data.task.remote

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)