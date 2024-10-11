package com.juandgaines.agenda.data.reminder.remote

import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.reminder.RemoteReminderDataSource
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.map
import javax.inject.Inject

class RetrofitRemoteReminderDataSource @Inject constructor(
    private val reminderApi: ReminderApi
): RemoteReminderDataSource {

    override suspend fun getReminderById(reminderId: String): Result<Reminder, Network> {
        return safeCall {
            reminderApi.getReminder(reminderId)
        }.map { reminderResponse ->
            reminderResponse.toReminder()
        }
    }
}