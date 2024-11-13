package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.Success
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import com.juandgaines.agenda.data.agenda.remote.SyncAgendaRequest
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.AlarmScheduler
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.toEpochMilli
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.mapError
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
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }
        val epochMillis = LocalDate.now().toEpochMilli()

        val response = safeCall {
            agendaApi.getAgenda(
                epochMillis
            )
        }.onSuccess {
            val tasks = it.tasks.map { task -> task.toTask() }
            val reminders = it.reminders.map { reminder -> reminder.toReminder() }

            syncTaskFromOtherDevices(epochMillis, tasks)
            syncReminderFromOtherDevices(epochMillis, reminders)

            taskRepository.upsertTasks(tasks)
            reminderRepository.upsertReminders(reminders)
        }

        return when (response) {
            is Error -> {
                response.error.toWorkerResult()
            }
            is com.juandgaines.core.domain.util.Result.Success -> {
                Result.success()
            }
        }
    }

    private suspend fun syncTaskFromOtherDevices(
        epochMillis: Long,
        tasks: List<Task>,
    ) {
        val localTaskList = taskRepository.getTasksAfterDate(epochMillis)
        val taskNotPresent = tasks.subtract(localTaskList.toSet()).toList()
        taskNotPresent.forEach { task ->
            alarmScheduler.scheduleAlarm(task)
        }
    }

    private suspend fun syncReminderFromOtherDevices(
        epochMillis: Long,
        reminders: List<Reminder>,
    ) {
        val localReminderList = reminderRepository.getRemindersAfterDate(epochMillis)
        val taskNotPresent = reminders.subtract(localReminderList.toSet()).toList()
        taskNotPresent.forEach { task ->
            alarmScheduler.scheduleAlarm(task)
        }
    }
}