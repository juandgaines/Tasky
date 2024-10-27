package com.juandgaines.agenda.data.reminder

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toReminderEntity
import com.juandgaines.agenda.data.mappers.toReminderRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderApi
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
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
    override suspend fun insertReminder(reminder: Reminder): Result<Unit, DataError> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)
            val response = safeCall {
                reminderApi.createReminder(reminder.toReminderRequest())
            }.onError {
                //TODO: Add to queue to create reminder later
            }.asEmptyDataResult()
            return response
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun updateReminder(reminder: Reminder): Result<Unit, DataError> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)

            val response = safeCall {
                reminderApi.updateReminder(reminder.toReminderRequest())
            }.onError {
                //TODO: Add to queue to update reminder later
            }.asEmptyDataResult()
            return response
        }
        catch (e: SQLiteException) {
            Result.Error(LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertReminders(list: List<Reminder>): Result<Unit, DataError> {
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

    override suspend fun getReminderById(reminderId: String): Result<Reminder, DataError> {
        val reminder = reminderDao.getReminderById(reminderId)?.toReminder()
        return reminder?.let {
            val response = safeCall {
                reminderApi.getReminderById(reminderId)
            }.map { data->
                data.toReminder()
            }.onError {
                //TODO: Add to queue to get task later
            }.onSuccess {
                applicationScope.async {
                    reminderDao.upsertReminder(it.toReminderEntity())
                }.await()
            }
            return response
        } ?: Result.Error(LocalError.NOT_FOUND)
    }

    override suspend fun deleteReminder(reminderId: String): Result<Unit, DataError> {
        reminderDao.deleteReminderById(reminderId)
        val response = safeCall {
            reminderApi.deleteReminderById(reminderId)
        }.onError {
                // TODO: Add to queue to delete reminder later
            }.asEmptyDataResult()
        return response
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