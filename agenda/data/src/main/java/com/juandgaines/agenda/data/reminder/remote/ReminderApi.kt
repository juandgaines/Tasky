package com.juandgaines.agenda.data.reminder.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ReminderApi {
    @POST("/reminder")
    suspend fun createReminder(@Body reminderRequest: ReminderRequest): Response<Unit>

    @PUT("/reminder")
    suspend fun updateReminder(@Body reminderRequest: ReminderRequest): Response<Unit>

    @GET("/reminder")
    suspend fun getReminder(@Query("reminderId") id:String): Response<ReminderResponse>

    @DELETE
    suspend fun deleteReminder(@Query("reminderId") id:String): Response<Unit>
}