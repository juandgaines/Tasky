package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import com.juandgaines.agenda.data.agenda.remote.SyncAgendaRequest
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.toEpochMilli
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class FetchAgendaWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val agendaApi: AgendaApi,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }

        val response = safeCall {
            val epochMillis = LocalDate.now().toEpochMilli()
            agendaApi.getAgenda(
                epochMillis
            )
        }.onSuccess {
            val tasks = it.tasks.map { task -> task.toTask() }
            val reminders = it.reminders.map { reminder -> reminder.toReminder() }

            taskRepository.upsertTasks(tasks)
            reminderRepository.upsertReminders(reminders)
        }

        return if (response is com.juandgaines.core.domain.util.Result.Error) {
            response.error.toWorkerResult()
        } else {
            Result.success()
        }
    }
}