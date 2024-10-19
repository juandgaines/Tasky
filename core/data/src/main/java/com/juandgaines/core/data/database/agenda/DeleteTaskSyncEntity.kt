package com.juandgaines.core.data.database.agenda

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delete_task_sync")
data class DeleteTaskSyncEntity (
    @PrimaryKey(autoGenerate = false)
    val taskId: String,
    val userId: String
)