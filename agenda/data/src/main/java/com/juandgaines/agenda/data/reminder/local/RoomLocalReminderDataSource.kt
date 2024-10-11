package com.juandgaines.agenda.data.reminder.local

import com.juandgaines.agenda.domain.reminder.LocalReminderDataSource
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalReminderDataSource @Inject constructor(
    private val reminderDao: ReminderDao
):LocalReminderDataSource {
    override suspend fun upsertReminder(reminder: Reminder): Result<String, LocalError> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertReminders(list: List<Reminder>): Result<List<String>, LocalError> {
        TODO("Not yet implemented")
    }

    override suspend fun getReminderById(reminderId: String): Result<Reminder, LocalError> {
        TODO("Not yet implemented")
    }

    override fun getTasks(): Flow<List<Reminder>> {
        TODO("Not yet implemented")
    }
}