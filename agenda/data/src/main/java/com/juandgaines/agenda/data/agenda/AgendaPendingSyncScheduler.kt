package com.juandgaines.agenda.data.agenda

import android.content.Context
import androidx.work.BackoffPolicy.EXPONENTIAL
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.juandgaines.agenda.data.agenda.workers.CreateAgendaItemWorker
import com.juandgaines.agenda.data.agenda.workers.DeleteAgendaItemWorker
import com.juandgaines.agenda.data.agenda.workers.FetchAgendaWorker
import com.juandgaines.agenda.data.agenda.workers.UpdateAgendaItemWorker
import com.juandgaines.agenda.data.mappers.toReminderEntity
import com.juandgaines.agenda.data.mappers.toTaskEntity
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.CreateAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.DeleteAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.FetchAgendas
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations.UpdateAgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.database.agenda.CreateReminderSyncEntity
import com.juandgaines.core.data.database.agenda.CreateTaskSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteReminderSyncEntity
import com.juandgaines.core.data.database.agenda.DeleteTaskSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateReminderSyncEntity
import com.juandgaines.core.data.database.agenda.UpdateTaskSyncEntity
import com.juandgaines.core.domain.auth.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class AgendaPendingSyncScheduler (
    private val context: Context,
    private val sessionStorage: SessionManager,
    private val agendaSyncDao: AgendaSyncDao,
    private val applicationScope: CoroutineScope
): AgendaSyncScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncType: AgendaSyncOperations) {
        when (syncType) {
            is CreateAgendaItem -> scheduleCreateAgendaItem(syncType.agendaItem)
            is DeleteAgendaItem -> scheduleDeleteAgendaItem(syncType.agendaItem)
            is FetchAgendas -> scheduleFetchAgendas(syncType.interval)
            is UpdateAgendaItem -> scheduleUpdateAgendaItem(syncType.agendaItem)
        }
    }

    private suspend fun scheduleFetchAgendas(interval: Duration){

        val isSyncScheduled = withContext(Dispatchers.IO){
            workManager.getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }
        if(isSyncScheduled){
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchAgendaWorker>(
            repeatInterval = interval.toJavaDuration(),
        )
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
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()

        workManager.enqueue(workRequest).await()
    }

    private suspend fun scheduleUpdateAgendaItem(agendaItem: AgendaItems) {
        val userId = sessionStorage.get()?.userId ?: return

        when (agendaItem) {
            is Reminder -> {
                agendaSyncDao.upsertUpdateReminderSync(
                    UpdateReminderSyncEntity(
                        reminder = agendaItem.toReminderEntity(),
                        userId = userId,
                        reminderId = agendaItem.id
                    )
                )
            }
            is Task -> {
                val task = agendaItem as Task
                agendaSyncDao.upsertUpdateTaskSync(
                    UpdateTaskSyncEntity(
                        task = task.toTaskEntity(),
                        userId = userId,
                        taskId = task.id
                    )
                )
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<UpdateAgendaItemWorker>()
            .addTag("update_work")
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
            .setInputData(
                Data.Builder()
                    .putString(UpdateAgendaItemWorker.AGENDA_ITEM_ID, agendaItem.id)
                    .build()
            )
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleCreateAgendaItem(agendaItem: AgendaItems) {
        val userId = sessionStorage.get()?.userId ?: return

        when (agendaItem) {
            is Reminder -> {
                agendaSyncDao.upsertCreateReminderSync(
                    CreateReminderSyncEntity(
                        reminder = agendaItem.toReminderEntity(),
                        userId = userId,
                        reminderId = agendaItem.id
                    )
                )
            }
            is Task -> {
                val task = agendaItem as Task
                agendaSyncDao.upsertCreateTaskSync(
                    CreateTaskSyncEntity(
                        task = task.toTaskEntity(),
                        userId = userId,
                        taskId = task.id
                    )
                )
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<CreateAgendaItemWorker>()
            .addTag("create_work")
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
            .setInputData(
                Data.Builder()
                    .putString(UpdateAgendaItemWorker.AGENDA_ITEM_ID, agendaItem.id)
                    .build()
            )
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }


    private suspend fun scheduleDeleteAgendaItem(agendaItem: AgendaItems) {
        val userId = sessionStorage.get()?.userId ?: return

        when (agendaItem) {

            is Reminder ->{
                agendaSyncDao.upsertDeleteReminderSync(
                    DeleteReminderSyncEntity(
                        reminderId = agendaItem.id,
                        userId = userId
                    )
                )
            }
            is Task -> {
                agendaSyncDao.upsertDeleteTaskSync(
                    DeleteTaskSyncEntity(
                        taskId = agendaItem.id,
                        userId = userId
                    )
                )
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<DeleteAgendaItemWorker>()
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