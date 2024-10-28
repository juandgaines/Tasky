package com.juandgaines.agenda.domain.reminder

import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun insertReminder(reminder: Reminder): Result<Unit, DataError>
    suspend fun updateReminder(reminder: Reminder): Result<Unit, DataError>
    suspend fun upsertReminders(list: List<Reminder>): Result<Unit, DataError>
    suspend fun getReminderById(reminderId: String): Result<Reminder, DataError>
    suspend fun deleteReminder(reminder:Reminder):Result<Unit, DataError>
    fun getReminders(
        startDate: Long,
        endDay: Long
    ): Flow<List<Reminder>>
}