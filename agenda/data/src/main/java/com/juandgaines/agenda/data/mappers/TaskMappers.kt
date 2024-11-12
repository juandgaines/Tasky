package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.task.remote.TaskRequest
import com.juandgaines.agenda.data.task.remote.TaskResponse
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.core.data.database.task.TaskEntity
import java.time.ZoneId

fun Task.toTaskRequest() = TaskRequest(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli(),
    isDone = isDone
)

fun TaskResponse.toTask() = Task(
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
        .withSecond(0),
    isDone = isDone
)

fun TaskEntity.toTask() = Task(
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
        .withSecond(0),
    isDone = isDone
)

fun Task.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli(),
    isDone = isDone
)