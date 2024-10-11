package com.juandgaines.agenda.domain.reminder

import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result

interface RemoteReminderDataSource {
    suspend fun getReminderById(reminderId:String): Result<Reminder, Network>
}