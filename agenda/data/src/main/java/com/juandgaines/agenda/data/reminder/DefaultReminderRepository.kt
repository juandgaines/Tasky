package com.juandgaines.agenda.data.reminder

import android.database.sqlite.SQLiteException
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toReminderEntity
import com.juandgaines.agenda.data.mappers.toReminderRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderApi
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import com.juandgaines.core.domain.util.onError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderApi: ReminderApi,
    private val applicationScope: CoroutineScope,
    private val agendaItemScheduler: AgendaSyncScheduler
):ReminderRepository {
    override suspend fun insertReminder(reminder: Reminder): Result<Unit, DataError> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)
            val response = safeCall {
                reminderApi.createReminder(reminder.toReminderRequest())
            }.onError {
                when (it) {
                    DataError.Network.NO_INTERNET -> {
                        agendaItemScheduler.scheduleSync(
                            AgendaSyncOperations.CreateAgendaItem(
                                reminder
                            )
                        )
                    }
                    else -> Unit
                }
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
                when (it) {
                    DataError.Network.NO_INTERNET -> {
                        agendaItemScheduler.scheduleSync(
                            AgendaSyncOperations.UpdateAgendaItem(
                                reminder
                            )
                        )
                    }
                    else -> Unit
                }
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
        return reminderDao.getReminderById(reminderId)?.toReminder()?.let {
            Result.Success(it)
        } ?: run {
            Result.Error(LocalError.NOT_FOUND)
        }
    }

    override fun getReminderByIdFlow(reminderId: String): Flow<Reminder?> {
        return reminderDao.getReminderByIdFlow(reminderId).map {
            it?.toReminder()
        }
    }

    override suspend fun deleteReminder(reminder: Reminder): Result<Unit, DataError> {
        val reminderId = reminder.id
        reminderDao.deleteReminderById(reminderId)
        val response = safeCall {
            reminderApi.deleteReminderById(reminderId)
        }.onError {
            when (it) {
                DataError.Network.NO_INTERNET ->
                    agendaItemScheduler.scheduleSync(
                        AgendaSyncOperations.DeleteAgendaItem(
                            reminder
                        )
                    )
                else -> Unit
            }
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