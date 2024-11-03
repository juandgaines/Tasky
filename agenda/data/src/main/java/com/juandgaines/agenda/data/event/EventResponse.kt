package com.juandgaines.agenda.data.event

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val id : String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)