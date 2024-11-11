package com.juandgaines.core.data.database.agenda

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juandgaines.core.data.database.task.TaskEntity

@Entity(tableName = "delete_task_sync")
data class DeleteTaskSyncEntity (
    @Embedded val task: TaskEntity,
    @PrimaryKey(autoGenerate = false)
    val taskId: String,
    val userId: String
)