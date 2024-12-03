package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import com.juandgaines.agenda.data.agenda.remote.SyncAgendaRequest
import com.juandgaines.agenda.data.mappers.toEvent
import com.juandgaines.agenda.domain.event.AttendeeRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeleteAgendaItemWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val agendaApi: AgendaApi,
    private val sessionManager: SessionManager,
    private val agendaSyncDao: AgendaSyncDao,
    private val attendeeRepository: AttendeeRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }
        val userId = sessionManager.get()?.userId ?: return Result.failure()

        val reminderDeleteList = agendaSyncDao.getAllDeleteReminderSync(userId)
        val taskDeleteList = agendaSyncDao.getAllDeleteTaskSync(userId)
        val eventDeleteList = agendaSyncDao.getAllDeleteEventSync(userId).filter {
            it.event.isUserEventCreator
        }
        val eventAttendeeDeleted = agendaSyncDao.getAllDeleteEventSync(userId).filter {
            !it.event.isUserEventCreator
        }

        val response = safeCall {
            agendaApi.syncAgenda(
                SyncAgendaRequest(
                    deletedEventIds = eventDeleteList.map { it.eventId },
                    deletedTaskIds = taskDeleteList.map { it.taskId },
                    deletedReminderIds = reminderDeleteList.map { it.reminderId }
                )
            )
        }.onSuccess {
            taskDeleteList.forEach {
                agendaSyncDao.deleteDeleteTaskSync(it.taskId)
            }
            reminderDeleteList.forEach {
                agendaSyncDao.deleteDeleteReminderSync(it.reminderId)
            }
            eventDeleteList.forEach {
                agendaSyncDao.deleteDeleteEventSync(it.eventId)
            }

            eventAttendeeDeleted.forEach {
                attendeeRepository.deleteAttendee(it.event.toEvent())
            }
        }

        return if (response is com.juandgaines.core.domain.util.Result.Error) {
            response.error.toWorkerResult()
        } else {
            Result.success()
        }
    }

    companion object {
        const val AGENDA_ITEM_ID = "itemId"
        const val AGENDA_ITEM_TYPE = "itemType"
    }
}