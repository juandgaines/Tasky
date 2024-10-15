package com.juandgaines.agenda.data.agenda

import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.ZonedDateTime
import javax.inject.Inject

class DefaultAgendaRepository @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository
) : AgendaRepository {
    override fun getItems(
        startDate: Long,
        endDay: Long
    ): Flow<List<AgendaItem>> = combine(
        reminderRepository.getReminders(
            startDate,
            endDay
        ),
        taskRepository.getTasks(
            startDate,
            endDay
        )
    ){ reminders, tasks ->
        (reminders + tasks)
    }
}