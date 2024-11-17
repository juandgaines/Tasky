package com.juandgaines.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.database.agenda.CreateReminderSyncEntity
import com.juandgaines.core.data.database.agenda.CreateTaskSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteReminderSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteTaskSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateReminderSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateTaskSyncEntity
import com.juandgaines.core.data.database.event.EventDao
import com.juandgaines.core.data.database.event.EventEntity
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.database.reminder.ReminderEntity
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.data.database.task.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        ReminderEntity::class,
        EventEntity::class,
        DeleteTaskSyncEntity::class,
        DeleteReminderSyncEntity::class,
        UpdateTaskSyncEntity::class,
        UpdateReminderSyncEntity::class,
        CreateTaskSyncEntity::class,
        CreateReminderSyncEntity::class
    ], version = 1)
abstract class TaskyDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun agendaSyncDao(): AgendaSyncDao
    abstract fun eventDao(): EventDao
}
