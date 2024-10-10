package com.juandgaines.core.data.database.reminder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    @ColumnInfo(name = "remind_at")
    val remindAt: Long,
)
