package com.juandgaines.agenda.data.event.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EventApi {
    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: RequestBody,
        @Part photos: List<MultipartBody.Part> = emptyList()
    ): Response<EventResponse>
}