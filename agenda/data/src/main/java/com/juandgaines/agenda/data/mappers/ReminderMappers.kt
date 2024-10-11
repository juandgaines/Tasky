package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.reminder.remote.ReminderRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderResponse
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.core.data.database.reminder.ReminderEntity

fun Reminder.toReminderRequest() = ReminderRequest(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt
)

fun ReminderResponse.toReminder() = Reminder(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt
)

fun ReminderEntity.toReminder() = Reminder(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt
)

fun Reminder.toReminderEntity() = ReminderEntity(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt
)

