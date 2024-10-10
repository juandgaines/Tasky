package com.juandgaines.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.database.reminder.ReminderEntity
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.data.database.task.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        ReminderEntity::class
    ], version = 1)
abstract class TaskyDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
}
