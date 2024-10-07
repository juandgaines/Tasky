package com.juandgaines.core.data.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase

@Database(entities = [TaskTestEntity::class], version = 1)
abstract class TaskyDataBase : RoomDatabase() {

}

//TODO: Remove this class, it is just to initialize the DB
@Entity
data class TaskTestEntity(
    @PrimaryKey
    val id: String,
)