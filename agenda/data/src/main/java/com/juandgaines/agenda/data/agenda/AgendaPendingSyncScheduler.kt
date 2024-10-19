package com.juandgaines.agenda.data.agenda

import android.content.Context
import androidx.work.BackoffPolicy.EXPONENTIAL
import androidx.work.Constraints
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.juandgaines.agenda.data.agenda.workers.DeleteAgendaTaskWorker
import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.CreateAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.DeleteAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.FetchAgendas
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.UpdateAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.agenda.AgendaType
import com.juandgaines.agenda.domain.agenda.AgendaType.Event
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.database.agenda.DeleteReminderSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteTaskSyncEntity
import com.juandgaines.core.domain.auth.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AgendaPendingSyncScheduler (
    private val context: Context,
    private val sessionStorage: SessionManager,
    private val agendaSyncDao: AgendaSyncDao,
    private val applicationScope: CoroutineScope
): AgendaSyncScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncType: AgendaSyncOperations) {
        when (syncType) {
            is CreateAgendaItem -> TODO()
            is DeleteAgendaItem -> scheduleDeleteAgendaItem(syncType.agendaItem)
            is FetchAgendas -> TODO()
            is UpdateAgendaItem -> TODO()
        }
    }

    private suspend fun scheduleDeleteAgendaItem(agendaItem: AgendaItem) {
        val userId = sessionStorage.get()?.userId ?: return

        when (agendaItem.type) {
            Event -> {

            }
            AgendaType.Reminder -> {
                agendaSyncDao.upsertDeleteReminderSync(
                    DeleteReminderSyncEntity(
                        reminderId = agendaItem.id,
                        userId = userId
                    )
                )
            }
            AgendaType.Task -> {
                agendaSyncDao.upsertDeleteTaskSync(
                    DeleteTaskSyncEntity(
                        taskId = agendaItem.id,
                        userId = userId
                    )
                )
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<DeleteAgendaTaskWorker>()
            .addTag("delete_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    override suspend fun cancelSync() {
        WorkManager
            .getInstance(context)
            .cancelAllWork()
            .await()
    }
}