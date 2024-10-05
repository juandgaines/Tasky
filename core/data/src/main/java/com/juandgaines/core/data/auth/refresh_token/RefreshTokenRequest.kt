package com.juandgaines.core.data.auth.refresh_token

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest (
    val refreshToken: String,
    val userId: String
)