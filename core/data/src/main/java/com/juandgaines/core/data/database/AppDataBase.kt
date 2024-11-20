package com.juandgaines.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.database.agenda.CreateEventSyncEntity
import com.juandgaines.core.data.database.agenda.CreateReminderSyncEntity
import com.juandgaines.core.data.database.agenda.CreateTaskSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteEventSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteReminderSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteTaskSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateEventSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateReminderSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateTaskSyncEntity
import com.juandgaines.core.data.database.event.EventConverters
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
        DeleteEventSyncEntity::class,
        UpdateTaskSyncEntity::class,
        UpdateReminderSyncEntity::class,
        UpdateEventSyncEntity::class,
        CreateTaskSyncEntity::class,
        CreateReminderSyncEntity::class,
        CreateEventSyncEntity::class
    ], version = 1)
@TypeConverters(EventConverters::class)
abstract class TaskyDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun agendaSyncDao(): AgendaSyncDao
    abstract fun eventDao(): EventDao
}
