package com.juandgaines.agenda.data.agenda

import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class DefaultAgendaRepository @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository
) : AgendaRepository {
    override fun getItems(date: Long): Flow<List<AgendaItem>> = combine(
        reminderRepository.getReminders(date),
        taskRepository.getTasks(date)
    ){ reminders, tasks ->
        (reminders + tasks).sortedBy { it.date}
    }
}