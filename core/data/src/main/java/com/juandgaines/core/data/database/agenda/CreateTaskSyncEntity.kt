package com.juandgaines.core.data.database.agenda

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juandgaines.core.data.database.task.TaskEntity

@Entity(tableName = "create_task_sync")
data class CreateTaskSyncEntity(
    @Embedded val task:TaskEntity,
    @PrimaryKey(autoGenerate = false)
    val taskId: String = task.id,
    val userId:String
)