package com.juandgaines.auth.data.network.signup

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("/login")
    suspend fun signUp(@Body registrationRequest: RegistrationRequest): Response<Unit>
}