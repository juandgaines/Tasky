package com.juandgaines.core.data.database.agenda

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juandgaines.core.data.database.event.EventEntity

@Entity(tableName = "create_event_sync")
data class CreateEventSyncEntity(
    @Embedded val event: EventEntity,
    @PrimaryKey(autoGenerate = false)
    val eventId: String = event.id,
    val userId:String
)