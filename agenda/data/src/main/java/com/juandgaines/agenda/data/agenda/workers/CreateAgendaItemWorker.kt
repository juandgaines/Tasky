package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CreateAgendaItemWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val agendaSyncDao: AgendaSyncDao,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }
        val agendaId = params.inputData.getString(AGENDA_ITEM_ID) ?: return Result.failure()
        val agendaType = params.inputData.getString(AGENDA_ITEM_TYPE) ?: return Result.failure()

        return when (agendaType) {
            Task::class.simpleName -> {
                val createTaskSync = agendaSyncDao.getCreateTaskSync(agendaId)
                createTaskSync?.let {
                    val response = taskRepository.insertTask(createTaskSync.task.toTask())

                    return if (response is com.juandgaines.core.domain.util.Result.Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateTaskSync(createTaskSync.taskId)
                        Result.success()
                    }
                } ?: Result.failure()
            }
            Reminder::class.simpleName -> {
                val createReminderSync = agendaSyncDao.getCreateReminderSync(agendaId)
                createReminderSync?.let {
                    val response = reminderRepository.insertReminder(createReminderSync.reminder.toReminder())

                    return if (response is com.juandgaines.core.domain.util.Result.Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateReminderSync(createReminderSync.reminderId)
                        Result.success()
                    }
                } ?: Result.failure()
            }
            else -> Result.success()
        }

    }

    companion object {
        const val AGENDA_ITEM_ID = "itemId"
        const val AGENDA_ITEM_TYPE = "itemType"
    }
}