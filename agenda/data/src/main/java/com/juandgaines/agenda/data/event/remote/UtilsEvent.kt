package com.juandgaines.agenda.data.event.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun createEventRequestBody(eventRequest: CreateEventRequest): RequestBody {
    val json = Json.encodeToString(CreateEventRequest.serializer(), eventRequest)
    return json.toRequestBody("application/json".toMediaType())
}

fun createPhotoParts(photoFiles: List<File>): List<MultipartBody.Part> {
    return photoFiles.mapIndexed { index, file ->
        val requestBody = file.readBytes().toRequestBody("image/jpeg".toMediaType())
        MultipartBody.Part.createFormData("photo$index", file.name, requestBody)
    }
}