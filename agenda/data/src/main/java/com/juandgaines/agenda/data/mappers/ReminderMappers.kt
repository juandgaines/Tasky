package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.reminder.remote.ReminderRequest
import com.juandgaines.agenda.data.reminder.remote.ReminderResponse
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.utils.toUtcZonedDateTime
import com.juandgaines.agenda.domain.utils.toZonedDateTimeWithZoneId
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
    description = description,
    time = time
        .toUtcZonedDateTime()
        .toZonedDateTimeWithZoneId(
            ZoneId.systemDefault()
        ),
    remindAt = remindAt
        .toUtcZonedDateTime()
        .toZonedDateTimeWithZoneId(
            ZoneId.systemDefault()
        )
)

fun ReminderEntity.toReminder() = Reminder(
    id = id,
    title = title,
    description = description,
    time = time.toUtcZonedDateTime()
        .toZonedDateTimeWithZoneId(
            ZoneId.systemDefault()
        ),
    remindAt = remindAt
        .toUtcZonedDateTime()
        .toZonedDateTimeWithZoneId(
            ZoneId.systemDefault()
        )
)

fun Reminder.toReminderEntity() = ReminderEntity(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli()
)

