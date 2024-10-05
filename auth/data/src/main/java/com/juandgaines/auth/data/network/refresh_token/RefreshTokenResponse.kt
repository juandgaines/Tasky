package com.juandgaines.auth.data.network.refresh_token

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val expirationTime: Long
)