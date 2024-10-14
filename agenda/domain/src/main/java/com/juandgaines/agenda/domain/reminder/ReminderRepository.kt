package com.juandgaines.agenda.domain.reminder

import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.core.domain.util.Error
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun insertReminder(reminder: Reminder): Result<Unit, Error>
    suspend fun updateReminder(reminder: Reminder): Result<Unit, Error>
    suspend fun upsertReminder(list: List<Reminder>): Result<Unit, Error>
    suspend fun getReminderById(reminderId: String): Result<Reminder, Error>
    suspend fun deleteReminder(reminderId: String):Result<Unit, Error>
    fun getReminders(): Flow<List<Reminder>>
}