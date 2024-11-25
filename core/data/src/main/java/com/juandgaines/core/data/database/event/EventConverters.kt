package com.juandgaines.core.data.database.event

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventConverters {
    @TypeConverter
    fun fromAttendeeList(attendees: List<AttendeeEntity>): String {
        return Json.encodeToString(attendees)
    }

    @TypeConverter
    fun toAttendeeList(attendeesJson: String): List<AttendeeEntity> {
        return Json.decodeFromString(attendeesJson)
    }
}