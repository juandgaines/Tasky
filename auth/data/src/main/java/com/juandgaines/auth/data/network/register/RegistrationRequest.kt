package com.juandgaines.auth.data.network.register

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val fullName: String,
    val email: String,
    val password: String
)
