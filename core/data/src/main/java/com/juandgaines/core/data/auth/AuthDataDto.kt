package com.juandgaines.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)