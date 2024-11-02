package com.juandgaines.core.data.database.agenda

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juandgaines.core.data.database.reminder.ReminderEntity

@Entity(tableName = "update_reminder_sync")
data class UpdateReminderSyncEntity(
    @Embedded val reminder:ReminderEntity,
    @PrimaryKey(autoGenerate = false)
    val reminderId: String = reminder.id,
    val userId:String
)