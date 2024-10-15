package com.juandgaines.agenda.data.reminder

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toReminderEntity
import com.juandgaines.agenda.data.mappers.toReminderRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderApi
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Error
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderApi: ReminderApi,
    private val applicationScope: CoroutineScope
):ReminderRepository {
    override suspend fun insertReminder(reminder: Reminder): Result<Unit, Error> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)
            val response = safeCall {
                reminderApi.createReminder(reminder.toReminderRequest())
            }
            when(response) {
                is Result.Success -> {
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    //TODO: Add to queue to create reminder later
                    Result.Error(response.error)
                }
            }
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun updateReminder(reminder: Reminder): Result<Unit, Error> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)

            val response = safeCall {
                reminderApi.updateReminder(reminder.toReminderRequest())
            }
            when (response) {
                is Result.Success -> {
                    Result.Success(Unit)
                }

                is Result.Error -> {
                    //TODO: Add to queue to update reminder later
                    Result.Error(response.error)
                }
            }
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertReminder(list: List<Reminder>): Result<Unit, Error> {
        return try {
            val entities = list.map { it.toReminderEntity() }
            applicationScope.async {
                reminderDao.upsertReminders(entities)
            }.await()
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun getReminderById(reminderId: String): Result<Reminder, Error> {
        val reminder = reminderDao.getReminderById(reminderId)?.toReminder()
        return reminder?.let {
            val response = safeCall {
                reminderApi.getReminderById(reminderId)
            }
            when (response) {
                is Result.Success -> {
                    Result.Success(response.data.toReminder())
                }
                is Result.Error -> {
                    Result.Error(response.error)
                }
            }
        } ?: Result.Error(LocalError.NOT_FOUND)
    }

    override suspend fun deleteReminder(reminderId: String): Result<Unit, Error> {
        reminderDao.deleteReminderById(reminderId)
        val response = safeCall {
            reminderApi.deleteReminderById(reminderId)
        }
        return when (response) {
            is Result.Success -> {
                Result.Success(Unit)
            }

            is Result.Error -> {
                // TODO: Add to queue to delete reminder later
                Result.Error(response.error)
            }
        }
    }

    override fun getReminders(
        startDate: Long,
        endDay: Long
    ): Flow<List<Reminder>> {
        return reminderDao
            .getReminders(
                startDate,
                endDay
            )
            .map { reminderEntities ->
                reminderEntities
                    .map {
                        it.toReminder()
                    }
            }
    }
}