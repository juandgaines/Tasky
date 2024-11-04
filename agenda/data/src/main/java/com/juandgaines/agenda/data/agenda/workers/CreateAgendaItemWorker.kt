package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.domain.agenda.AgendaItemOption
import com.juandgaines.core.domain.agenda.AgendaItemOption.REMINDER
import com.juandgaines.core.domain.agenda.AgendaItemOption.TASK
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
        val type = params.inputData.getInt(UpdateAgendaItemWorker.AGENDA_ITEM_TYPE,-1)
        if (type == -1) return Result.failure()
        val agendaType= AgendaItemOption.fromOrdinal(type)

        return when (agendaType) {
            TASK -> {

                val createTaskSync = agendaSyncDao.getCreateTaskSync(agendaId)
                createTaskSync?.let {
                    val updateTaskSync = agendaSyncDao.getUpdateTaskSync(createTaskSync.taskId)

                    val createdTask = updateTaskSync?.task ?: createTaskSync.task

                    val response = taskRepository.insertTask(createdTask.toTask())
                    return if (response is com.juandgaines.core.domain.util.Result.Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateTaskSync(createTaskSync.taskId)
                        updateTaskSync?.let {
                            agendaSyncDao.deleteUpdateTaskSync(it.taskId)
                        }
                        Result.success()
                    }
                } ?: Result.failure()
            }
            REMINDER -> {
                val createReminderSync = agendaSyncDao.getCreateReminderSync(agendaId)
                createReminderSync?.let {

                    val updatedReminderSync = agendaSyncDao.getUpdateReminderSync(createReminderSync.reminderId)

                    val createReminder = updatedReminderSync?.reminder ?: createReminderSync.reminder


                    val response = reminderRepository.insertReminder(createReminder.toReminder())

                    return if (response is com.juandgaines.core.domain.util.Result.Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateReminderSync(createReminderSync.reminderId)
                        updatedReminderSync?.let {
                            agendaSyncDao.deleteUpdateReminderSync(it.reminderId)
                        }
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