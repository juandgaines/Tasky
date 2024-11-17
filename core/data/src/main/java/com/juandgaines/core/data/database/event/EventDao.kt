package com.juandgaines.core.data.database.event

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Upsert
    suspend fun upsertEvent(reminderEntity: EventEntity)

    @Upsert
    suspend fun upsertEvents(reminderEntities: List<EventEntity>)

    @Query("SELECT * FROM event WHERE id=:id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT * FROM event WHERE id=:id")
    fun getEventByIdFlow(id: String): Flow<EventEntity?>

    @Query("DELETE FROM event WHERE id=:id")
    suspend fun deleteEventById(id: String)

    @Query("SELECT * FROM event WHERE time >= :startDate AND time <= :endDay")
    fun getEvents(
        startDate: Long,
        endDay: Long
    ): Flow<List<EventEntity>>

    @Query("SELECT * FROM event WHERE time >= :date")
    suspend fun getEventsAfterDate(date: Long): List<EventEntity>
}