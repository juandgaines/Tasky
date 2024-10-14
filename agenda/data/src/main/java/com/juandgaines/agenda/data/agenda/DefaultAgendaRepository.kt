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
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository
) : AgendaRepository {

}