package com.juandgaines.agenda.data.event.remote

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface AttendeeApi {
    @GET("/attendee")
    suspend fun getAttendee(
        @Query("email") email: String,
    ): Response<AttendeeResponse>

    @DELETE("/attendee")
    suspend fun deleteAttendee(
        @Query("eventId") eventId: String,
    ): Response<Unit>
}