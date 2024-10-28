package com.juandgaines.core.data.database.task

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(taskEntity: TaskEntity)

    @Upsert
    suspend fun upsertTasks(taskEntities: List<TaskEntity>)

    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun getTaskById(id: String): TaskEntity?

    @Query("SELECT * FROM task WHERE id=:id")
    fun getTaskByIdFlow(id: String): Flow<TaskEntity?>

    @Query("DELETE FROM task WHERE id=:id")
    suspend fun deleteTask(id: String)

    @Query("SELECT * FROM task WHERE time >= :startDate AND time <= :endDay")
    fun getTasks(
        startDate: Long,
        endDay: Long
    ): Flow<List<TaskEntity>>
}