package com.juandgaines.auth.data.network.auth.signup

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val fullName: String,
    val email: String,
    val password: String
)
