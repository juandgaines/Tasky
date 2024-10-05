package com.juandgaines.core.data.auth

import com.juandgaines.core.data.auth.refresh_token.RefreshTokenRequest
import com.juandgaines.core.data.auth.refresh_token.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {
    @POST("/accessToken")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("/authenticate")
    suspend fun checkAuth(): Response<Unit>

    @POST("/logout")
    suspend fun logout(): Response<Unit>
}