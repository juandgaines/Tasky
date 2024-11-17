package com.juandgaines.agenda.data.event.remote

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>,
    val photos: List<PhotoDto>? = null
)

@Serializable
data class AttendeeDto(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

@Serializable
data class PhotoDto(
    val key: String,
    val url: String
)

