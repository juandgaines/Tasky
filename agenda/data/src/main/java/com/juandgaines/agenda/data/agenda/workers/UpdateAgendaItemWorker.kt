package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.mappers.toEvent
import com.juandgaines.agenda.data.mappers.toReminder
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.event.EventRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.domain.agenda.AgendaItemOption
import com.juandgaines.core.domain.util.Result.Error
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateAgendaItemWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val agendaSyncDao: AgendaSyncDao,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val eventRepository: EventRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }
        val agendaId = params.inputData.getString(AGENDA_ITEM_ID) ?: return Result.failure()
        val type = params.inputData.getInt(AGENDA_ITEM_TYPE,-1)
        if (type == -1) return Result.failure()
        val agendaType= AgendaItemOption.fromOrdinal(type)

        return  when (agendaType) {
            AgendaItemOption.TASK-> {

                val taskResponse = taskRepository.getTaskById(agendaId)

                if (taskResponse is Error) {
                   // needs to be created
                    val taskCreated = agendaSyncDao.getCreateTaskSync(agendaId) ?: return Result.failure()
                    val updateTask = agendaSyncDao.getUpdateTaskSync(agendaId) ?: return Result.failure()
                    val task = updateTask.task
                    val response = taskRepository.insertTask(task.toTask())

                    return if (response is Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateTaskSync(taskCreated.taskId)
                        agendaSyncDao.deleteUpdateTaskSync(updateTask.taskId)
                        Result.success()
                    }
                }
                else{
                    updateAgendaItemTaskPendingSync(agendaId)
                }

            }
            AgendaItemOption.REMINDER -> {
                val reminderResponse = reminderRepository.getReminderById(agendaId)

                if (reminderResponse is Error) {
                    val reminderCreated = agendaSyncDao.getCreateReminderSync(agendaId) ?: return Result.failure()
                    val updateReminder = agendaSyncDao.getUpdateReminderSync(agendaId) ?: return Result.failure()
                    val reminder = updateReminder.reminder
                    val response = reminderRepository.insertReminder(reminder.toReminder())

                    return if (response is Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateReminderSync(reminderCreated.reminderId)
                        agendaSyncDao.deleteUpdateReminderSync(updateReminder.reminderId)
                        Result.success()
                    }
                }
                else{
                    updateAgendaItemReminderSync(agendaId)
                }
            }
            AgendaItemOption.EVENT -> {
                val eventResponse = eventRepository.getEventById(agendaId)
                if (eventResponse is Error) {
                    val eventCreated = agendaSyncDao.getCreateEventSync(agendaId) ?: return Result.failure()
                    val updateEvent = agendaSyncDao.getUpdateEventSync(agendaId) ?: return Result.failure()
                    val event = updateEvent.event
                    val response = eventRepository.insertEvent(event.toEvent(), emptyList())

                    return if (response is Error) {
                        response.error.toWorkerResult()
                    } else {
                        agendaSyncDao.deleteCreateEventSync(eventCreated.eventId)
                        agendaSyncDao.deleteUpdateEventSync(updateEvent.eventId)
                        Result.success()
                    }
                }
                else{
                    updateAgendaItemEventPendingSync(agendaId)
                }
            }
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

    private suspend fun updateAgendaItemEventPendingSync(agendaId: String): Result {
        val updatedPendingAgendaItem = agendaSyncDao.getUpdateEventSync(agendaId)
        return updatedPendingAgendaItem?.let {
            val response = eventRepository.updateEvent(
                updatedPendingAgendaItem.event.toEvent(), emptyList()
            )
            return if (response is Error) {
                response.error.toWorkerResult()
            } else {
                agendaSyncDao.deleteUpdateEventSync(updatedPendingAgendaItem.eventId)
                Result.success()
            }
        } ?: Result.failure()
    }

    companion object {
        const val AGENDA_ITEM_ID = "itemId"
        const val AGENDA_ITEM_TYPE = "itemType"
    }
}