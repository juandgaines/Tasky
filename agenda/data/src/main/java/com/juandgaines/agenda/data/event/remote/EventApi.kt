package com.juandgaines.agenda.data.event.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface EventApi {
    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: RequestBody,
        @Part photos: List<MultipartBody.Part> = emptyList()
    ): Response<EventResponse>

    @Multipart
    @PUT("/event")
    suspend fun updateEvent(
        @Part("update_event_request") updateEventRequest: RequestBody,
        @Part photos: List<MultipartBody.Part> = emptyList()
    ): Response<EventResponse>

    @DELETE("/event")
    suspend fun deleteEventById(@Query("eventId") id:String): Response<Unit>
}