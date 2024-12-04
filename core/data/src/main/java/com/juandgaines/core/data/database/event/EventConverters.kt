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

class PhotoConverters {
    @TypeConverter
    fun fromAttendeeList(photoJson: List<PhotoEntity>): String {
        return Json.encodeToString(photoJson)
    }

    @TypeConverter
    fun toAttendeeList(photoJson: String): List<PhotoEntity> {
        return Json.decodeFromString(photoJson)
    }
}