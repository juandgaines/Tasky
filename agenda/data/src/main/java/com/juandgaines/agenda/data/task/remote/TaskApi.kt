package com.juandgaines.agenda.data.task.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskApi{
    @POST("/task")
    suspend fun createTask(@Body taskRequest: TaskRequest): Response<Unit>

    @PUT("/task")
    suspend fun updateTask(@Body taskRequest: TaskRequest): Response<Unit>

    @GET("/task")
    suspend fun getTaskById(@Query("taskId") id: String): Response<TaskRequest>

    @DELETE("/task")
    suspend fun deleteTask(@Query("taskId") id: String): Response<Unit>
}