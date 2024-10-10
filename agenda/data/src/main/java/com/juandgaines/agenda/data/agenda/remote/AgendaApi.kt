package com.juandgaines.agenda.data.agenda.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AgendaApi {
    @GET("/agenda")
    suspend fun getAgenda( @Query("time") time: Long): Response<AgendaResponse>

    @POST("/syncAgenda")
    suspend fun syncAgenda(@Body syncAgendaRequest: SyncAgendaRequest): Response<Unit>

    @GET("/fullAgenda")
    suspend fun getFullAgenda(): Response<AgendaResponse>
}