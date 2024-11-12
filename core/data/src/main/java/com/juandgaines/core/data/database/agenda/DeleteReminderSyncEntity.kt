package com.juandgaines.core.data.database.agenda

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juandgaines.core.data.database.reminder.ReminderEntity

@Entity (tableName = "delete_reminder_sync")
data class DeleteReminderSyncEntity (
    @Embedded val reminder: ReminderEntity,
    @PrimaryKey(autoGenerate = false)
    val reminderId: String,
    val userId: String
)