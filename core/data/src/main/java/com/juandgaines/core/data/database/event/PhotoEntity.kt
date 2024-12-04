package com.juandgaines.core.data.database.event

import kotlinx.serialization.Serializable

@Serializable
data class PhotoEntity(
    val key: String?,
    val url: String
)

