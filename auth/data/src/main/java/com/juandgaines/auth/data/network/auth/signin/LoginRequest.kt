package com.juandgaines.auth.data.network.auth.signin

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)