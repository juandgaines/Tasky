package com.juandgaines.core.data.database.agenda

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AgendaSyncDao{

    //Task sync delete
    @Query ("SELECT * FROM delete_task_sync where userId = :userId")
    suspend fun getAllDeleteTaskSync(userId:String): List<DeleteTaskSyncEntity>

    @Upsert
    suspend fun upsertDeleteTaskSync(deleteTaskSync: DeleteTaskSyncEntity)

    @Query("DELETE FROM delete_task_sync WHERE taskId = :taskId")
    suspend fun deleteDeleteTaskSync(taskId: String)

    //Task sync update
    @Upsert
    suspend fun upsertUpdateTaskSync(updateTaskSync: UpdateTaskSyncEntity)

    @Query("SELECT * FROM update_task_sync WHERE userId = :userId")
    suspend fun getAllUpdateTaskSync(userId: String): List<UpdateTaskSyncEntity>

    @Query("DELETE FROM update_task_sync WHERE taskId = :taskId")
    suspend fun deleteUpdateTaskSync(taskId: String)

    @Query("SELECT * FROM update_task_sync WHERE taskId = :taskId")
    suspend fun getUpdateTaskSync(taskId: String): UpdateTaskSyncEntity?


    //Task sync create
    @Upsert
    suspend fun upsertCreateTaskSync(updateTaskSync: CreateTaskSyncEntity)

    @Query("SELECT * FROM create_task_sync WHERE userId = :userId")
    suspend fun getAllCreateTaskSync(userId: String): List<CreateTaskSyncEntity>

    @Query("DELETE FROM create_task_sync WHERE taskId = :taskId")
    suspend fun deleteCreateTaskSync(taskId: String)

    @Query("SELECT * FROM create_task_sync WHERE taskId = :taskId")
    suspend fun getCreateTaskSync(taskId: String): CreateTaskSyncEntity?

    //Reminder sync delete

    @Query("SELECT * FROM delete_reminder_sync WHERE userId = :userId")
    suspend fun getAllDeleteReminderSync(userId: String): List<DeleteReminderSyncEntity>

    @Upsert
    suspend fun upsertDeleteReminderSync(deleteReminderSync: DeleteReminderSyncEntity)

    @Query("DELETE FROM delete_reminder_sync WHERE reminderId = :reminderId")
    suspend fun deleteDeleteReminderSync(reminderId: String)

    //Reminder sync update
    @Upsert
    suspend fun upsertUpdateReminderSync(updateTaskSync: UpdateReminderSyncEntity)

    @Query("SELECT * FROM update_reminder_sync WHERE userId = :userId")
    suspend fun getAllUpdateReminderSync(userId: String): List<UpdateReminderSyncEntity>

    @Query("DELETE FROM update_reminder_sync WHERE reminderId = :taskId")
    suspend fun deleteUpdateReminderSync(taskId: String)

    @Query("SELECT * FROM update_reminder_sync WHERE reminderId = :taskId")
    suspend fun getUpdateReminderSync(taskId: String): UpdateReminderSyncEntity?

    //Reminder sync create
    @Upsert
    suspend fun upsertCreateReminderSync(updateTaskSync: CreateReminderSyncEntity)

    @Query("SELECT * FROM create_reminder_sync WHERE userId = :userId")
    suspend fun getAllCreateReminderSync(userId: String): List<CreateReminderSyncEntity>

    @Query("DELETE FROM create_reminder_sync WHERE reminderId = :taskId")
    suspend fun deleteCreateReminderSync(taskId: String)

    @Query("SELECT * FROM create_reminder_sync WHERE reminderId = :taskId")
    suspend fun getCreateReminderSync(taskId: String): CreateReminderSyncEntity?


    //Event sync create
    @Upsert
    suspend fun upsertCreateEventSync(createEventSyncEntity: CreateEventSyncEntity)

    @Query("SELECT * FROM create_event_sync WHERE userId = :userId")
    suspend fun getAllCreateEventSync(userId: String): List<CreateEventSyncEntity>

    @Query("DELETE FROM create_event_sync WHERE eventId = :taskId")
    suspend fun deleteCreateEventSync(taskId: String)

    @Query("SELECT * FROM create_event_sync WHERE eventId = :taskId")
    suspend fun getCreateEventSync(taskId: String): CreateEventSyncEntity?

    //Event sync update
    @Upsert
    suspend fun upsertUpdateEventSync(updateEventSync: UpdateEventSyncEntity)

    @Query("SELECT * FROM update_event_sync WHERE userId = :userId")
    suspend fun getAllUpdateEventSync(userId: String): List<UpdateEventSyncEntity>

    @Query("DELETE FROM update_event_sync WHERE eventId = :taskId")
    suspend fun deleteUpdateEventSync(taskId: String)

    @Query("SELECT * FROM update_event_sync WHERE eventId = :taskId")
    suspend fun getUpdateEventSync(taskId: String): UpdateEventSyncEntity?


    //Event sync delete
    @Query("SELECT * FROM delete_event_sync WHERE userId = :userId")
    suspend fun getAllDeleteEventSync(userId: String): List<DeleteEventSyncEntity>

    @Upsert
    suspend fun upsertDeleteEventSync(deleteEventSyncEntity: DeleteEventSyncEntity)

    @Query("DELETE FROM delete_event_sync WHERE eventId = :reminderId")
    suspend fun deleteDeleteEventSync(reminderId: String)
}