package com.juandgaines.core.domain

data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
