package com.juandgaines.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataDto(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long,
)