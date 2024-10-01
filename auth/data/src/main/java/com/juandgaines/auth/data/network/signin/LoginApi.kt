package com.juandgaines.auth.data.network.signin

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}