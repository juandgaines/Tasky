package com.juandgaines.core.data.database.agenda

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AgendaSyncDao{

    @Query ("SELECT * FROM delete_task_sync where userId = :userId")
    suspend fun getAllDeleteTaskSync(userId:String): List<DeleteTaskSyncEntity>

    @Upsert
    suspend fun upsertDeleteTaskSync(deleteTaskSync: DeleteTaskSyncEntity)

    @Query("DELETE FROM delete_task_sync WHERE taskId = :taskId")
    suspend fun deleteDeleteTaskSync(taskId: String)

    @Query("SELECT * FROM delete_reminder_sync WHERE userId = :userId")
    suspend fun getAllDeleteReminderSync(userId: String): List<DeleteReminderSyncEntity>

    @Upsert
    suspend fun upsertDeleteReminderSync(deleteReminderSync: DeleteReminderSyncEntity)

    @Query("DELETE FROM delete_reminder_sync WHERE reminderId = :reminderId")
    suspend fun deleteDeleteReminderSync(reminderId: String)

}