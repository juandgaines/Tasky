package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.task.remote.TaskRequest
import com.juandgaines.agenda.data.task.remote.TaskResponse
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.core.data.database.task.TaskEntity

fun Task.toTaskRequest() = TaskRequest(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt,
    isDone = isDone
)

fun TaskResponse.toTask() = Task(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt,
    isDone = isDone
)

fun TaskEntity.toTask() = Task(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt,
    isDone = isDone
)

fun Task.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    time = time,
    remindAt = remindAt,
    isDone = isDone
)