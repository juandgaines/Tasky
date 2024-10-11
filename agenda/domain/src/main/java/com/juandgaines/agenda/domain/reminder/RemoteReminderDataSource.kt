package com.juandgaines.agenda.domain.reminder

import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result

interface RemoteReminderDataSource {
    suspend fun createReminder(reminder: Reminder): Result<Unit,Network>
    suspend fun updateReminder(reminder: Reminder): Result<Unit,Network>
    suspend fun getReminderById(reminderId:String): Result<Reminder, Network>
    suspend fun deleteReminder(reminderId:String): Result<Unit,Network>
}