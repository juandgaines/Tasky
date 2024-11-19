package com.juandgaines.agenda.data.event.remote

import kotlinx.serialization.Serializable

@Serializable
data class AttendeeResponse(
    val doesUserExist: Boolean,
    val attendee: AttendeeMinimalDto
)

@Serializable
data class AttendeeMinimalDto(
    val email: String,
    val fullName: String,
    val userId: String,
)

