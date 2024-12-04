package com.juandgaines.core.data.database.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val timeEnd: Long,
    @ColumnInfo(name = "remind_at")
    val remindAt: Long,
    val host : String,
    @ColumnInfo(name = "is_user_event_creator")
    val isUserEventCreator: Boolean,
    @ColumnInfo(name = "is_going")
    val isGoing: Boolean,
    val photos: List<PhotoEntity>,
    val attendees: List<AttendeeEntity>
)