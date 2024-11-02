package com.juandgaines.agenda.data.agenda.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juandgaines.agenda.data.mappers.toTask
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.domain.auth.SessionManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CreateAgendaItemWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val agendaSyncDao: AgendaSyncDao,
    private val taskRepository: TaskRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }
        val agendaId = params.inputData.getString(AGENDA_ITEM_ID) ?: return Result.failure()

        val updatedPendingAgendaItem = agendaSyncDao.getUpdateTaskSync(agendaId)


        return  when {
            updatedPendingAgendaItem != null -> {
                val response = taskRepository.updateTask(updatedPendingAgendaItem.task.toTask())

                return if (response is com.juandgaines.core.domain.util.Result.Error) {
                    response.error.toWorkerResult()
                } else {
                    agendaSyncDao.deleteUpdateTaskSync(updatedPendingAgendaItem.taskId)
                    Result.success()
                }
            }
            else -> Result.failure()
        }
    }

    companion object {
        const val AGENDA_ITEM_ID = "itemId"
    }
}