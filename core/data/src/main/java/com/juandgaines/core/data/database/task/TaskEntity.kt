package com.juandgaines.core.data.database.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    @ColumnInfo(name = "remind_at")
    val remindAt: Long,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean
)
