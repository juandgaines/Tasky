package com.juandgaines.auth.data.network

import com.juandgaines.auth.data.network.login.LoginRequest
import com.juandgaines.auth.data.network.login.LoginResponse
import com.juandgaines.auth.data.network.register.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<Unit>

    @POST("/logout")
    suspend fun logout(): Response<Unit>
}