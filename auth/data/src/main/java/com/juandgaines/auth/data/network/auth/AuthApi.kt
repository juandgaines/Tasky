package com.juandgaines.auth.data.network.auth

import com.juandgaines.auth.data.network.auth.signin.LoginRequest
import com.juandgaines.auth.data.network.auth.signin.LoginResponse
import com.juandgaines.auth.data.network.auth.signup.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun signUp(@Body registrationRequest: RegistrationRequest): Response<Unit>
}