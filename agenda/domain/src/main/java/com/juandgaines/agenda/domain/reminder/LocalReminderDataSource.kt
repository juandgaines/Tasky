package com.juandgaines.agenda.domain.reminder

import com.juandgaines.core.domain.util.DataError.LocalError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface LocalReminderDataSource {
    suspend fun upsertReminder(reminder: Reminder):Result<String,LocalError>
    suspend fun upsertReminders(list:List<Reminder>):Result<List<String>,LocalError>
    suspend fun getReminderById(reminderId:String):Result<Reminder,LocalError>
    fun getTasks(): Flow<List<Reminder>>
}