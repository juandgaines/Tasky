package com.juandgaines.agenda.data.agenda

import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.asEmptyDataResult
import javax.inject.Inject

class DefaultAgendaRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val sessionManager: SessionManager,
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository
) : AgendaRepository {
    override suspend fun logout(): Result<Unit, Network> {
        val response = safeCall {
            tokenApi.logout()
        }
        if (response is Result.Success){
            sessionManager.set(null)
        }
        return response
    }
}