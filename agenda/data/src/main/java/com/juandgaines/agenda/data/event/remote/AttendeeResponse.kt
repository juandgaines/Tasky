package com.juandgaines.agenda.data.event.remote

import kotlinx.serialization.Serializable

@Serializable
data class AttendeeResponse(
    val doesUserExist: Boolean,
    val attendee: AttendeeRDto
)

@Serializable
data class AttendeeRDto(
    val email: String,
    val fullName: String,
    val userId: String,
)

