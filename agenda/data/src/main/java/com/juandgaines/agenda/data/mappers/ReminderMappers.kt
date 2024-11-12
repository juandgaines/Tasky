package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.reminder.remote.ReminderRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderResponse
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder

import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.core.data.database.reminder.ReminderEntity
import java.time.ZoneId

fun Reminder.toReminderRequest() = ReminderRequest(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli()
)

fun ReminderResponse.toReminder() = Reminder(
    id = id,
    title = title,
    description = description.orEmpty(),
    time = time
        .toZonedDateTime(
            zoneId = ZoneId.systemDefault()
        ).withNano(0)
        .withSecond(0),
    remindAt = remindAt
        .toZonedDateTime(
            zoneId = ZoneId.systemDefault()
        ).withNano(0)
        .withSecond(0)
)

fun ReminderEntity.toReminder() = Reminder(
    id = id,
    title = title,
    description = description.orEmpty(),
    time = time
        .toZonedDateTime(
            zoneId = ZoneId.systemDefault()
        ).withNano(0)
        .withSecond(0),
    remindAt = remindAt
        .toZonedDateTime(
            zoneId = ZoneId.systemDefault()
        ).withNano(0)
        .withSecond(0)
)

fun Reminder.toReminderEntity() = ReminderEntity(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli()
)

