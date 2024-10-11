package com.juandgaines.agenda.data.reminder.remote

import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.reminder.RemoteReminderDataSource
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import javax.inject.Inject

class RetrofitRemoteReminderDataSource @Inject constructor(
    private val reminderApi: ReminderApi
): RemoteReminderDataSource {
    override suspend fun createReminder(reminder: Reminder): Result<Unit, Network> {
        TODO("Not yet implemented")
    }

    override suspend fun updateReminder(reminder: Reminder): Result<Unit, Network> {
        TODO("Not yet implemented")
    }

    override suspend fun getReminderById(reminderId: String): Result<Reminder, Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReminder(reminderId: String): Result<Unit, Network> {
        TODO("Not yet implemented")
    }
}