package com.juandgaines.core.data.database.reminder

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Upsert
    suspend fun upsertReminder(reminderEntity: ReminderEntity)

    @Upsert
    suspend fun upsertReminders(reminderEntities: List<ReminderEntity>)

    @Query("SELECT * FROM reminder WHERE id=:id")
    suspend fun getReminderById(id: String): ReminderEntity?

    @Query("SELECT * FROM reminder WHERE id=:id")
    fun getReminderByIdFlow(id: String): Flow<ReminderEntity?>

    @Query("DELETE FROM reminder WHERE id=:id")
    suspend fun deleteReminderById(id: String)

    @Query("SELECT * FROM reminder WHERE time >= :startDate AND time <= :endDay")
    fun getReminders(
        startDate: Long,
        endDay: Long
    ): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder WHERE time >= :date")
    suspend fun getRemindersAfterDate(date: Long): List<ReminderEntity>
}