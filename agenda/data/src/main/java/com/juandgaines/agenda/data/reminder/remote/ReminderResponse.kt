package com.juandgaines.agenda.data.reminder.remote

import kotlinx.serialization.Serializable

@Serializable
data class ReminderResponse(
    val id : String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)