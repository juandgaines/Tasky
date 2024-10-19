package com.juandgaines.core.data.database.agenda

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "delete_reminder_sync")
data class DeleteReminderSyncEntity (
    @PrimaryKey(autoGenerate = false)
    val reminderId: String,
    val userId: String
)