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
import com.juandgaines.core.domain.util.Result.Error
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateAgendaItemWorker @AssistedInject constructor(
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

        return  when (agendaType) {
            Task::class.simpleName-> {
                val taskCreated= agendaSyncDao.getCreateTaskSync(agendaId)

                taskCreated?.let {
                    val response = taskRepository.insertTask(taskCreated.task.toTask())
                    return if (response is com.juandgaines.core.domain.util.Result.Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateTaskSync(taskCreated.taskId)
                        updateAgendaItemTaskPendingSync(agendaId)
                    }
                }?:run {
                    updateAgendaItemTaskPendingSync(agendaId)
                }
            }
            Reminder::class.simpleName -> {
                val reminderCreated = agendaSyncDao.getCreateReminderSync(agendaId)
                reminderCreated?.let {
                    val response = reminderRepository.insertReminder(reminderCreated.reminder.toReminder())
                    return if (response is com.juandgaines.core.domain.util.Result.Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateReminderSync(reminderCreated.reminderId)
                        updateAgendaItemReminderSync(agendaId)
                    }
                }?:run {
                    updateAgendaItemReminderSync(agendaId)
                }
                updateAgendaItemReminderSync(agendaId)
            }
            else -> Result.failure()
        }
    }

    private suspend fun updateAgendaItemReminderSync(agendaId: String): Result {
        val updatedPendingAgendaItem = agendaSyncDao.getUpdateReminderSync(agendaId)
        return updatedPendingAgendaItem?.let {
            val response = reminderRepository.updateReminder(
                updatedPendingAgendaItem.reminder.toReminder()
            )

            return if (response is Error) {
                response.error.toWorkerResult()
            } else {
                agendaSyncDao.deleteUpdateReminderSync(updatedPendingAgendaItem.reminderId)
                Result.success()
            }
        } ?: Result.failure()
    }

    private suspend fun updateAgendaItemTaskPendingSync(agendaId: String): Result {
        val updatedPendingAgendaItem = agendaSyncDao.getUpdateTaskSync(agendaId)
        return updatedPendingAgendaItem?.let {
            val response = taskRepository.updateTask(updatedPendingAgendaItem.task.toTask())

            return if (response is Error) {
                response.error.toWorkerResult()
            } else {
                agendaSyncDao.deleteUpdateTaskSync(updatedPendingAgendaItem.taskId)
                Result.success()
            }
        } ?: Result.failure()
    }

    companion object {
        const val AGENDA_ITEM_ID = "itemId"
        const val AGENDA_ITEM_TYPE = "itemType"
    }
}