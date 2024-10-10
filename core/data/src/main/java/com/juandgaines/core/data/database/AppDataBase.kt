package com.juandgaines.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juandgaines.core.data.database.local.TaskDao
import com.juandgaines.core.data.database.local.TaskEntity

@Database(
    entities = [
        TaskEntity::class
    ], version = 1)
abstract class TaskyDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
