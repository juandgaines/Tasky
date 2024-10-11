package com.juandgaines.agenda.data.reminder.local

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toReminderEntity
import com.juandgaines.agenda.domain.reminder.LocalReminderDataSource
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalReminderDataSource @Inject constructor(
    private val reminderDao: ReminderDao
):LocalReminderDataSource {
    override suspend fun upsertReminder(reminder: Reminder): Result<String, LocalError> {
        return try{
            reminderDao.upsertReminder(reminder.toReminderEntity())
            Result.Success(reminder.id)
        }catch (e:SQLiteException){
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertReminders(list: List<Reminder>): Result<List<String>, LocalError> {
        return try{
            val entities = list.map { it.toReminderEntity() }
            reminderDao.upsertReminders(entities)
            Result.Success(entities.map { it.id })
        }catch (e:SQLiteException){
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun getReminderById(reminderId: String): Result<Reminder, LocalError> {
        val reminderEntity = reminderDao.getReminderById(reminderId)
        return reminderEntity?.let {
            Result.Success(reminderEntity.toReminder())
        }?: Result.Error(LocalError.NOT_FOUND)
    }

    override fun getTasks(): Flow<List<Reminder>> {
        return reminderDao
            .getReminders()
            .map { reminderEntities ->
                reminderEntities.map {
                    it.toReminder()
                }
            }
    }
}