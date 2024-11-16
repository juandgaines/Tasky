package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.event.remote.CreateEventRequest
import com.juandgaines.agenda.data.event.remote.EventResponse
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.core.data.database.event.EventEntity

fun EventResponse.toEvent() = Event(
    id = id,
    title = title,
    description = description,
    time = from.toZonedDateTime(),
    endTime = to.toZonedDateTime(),
    remindAt = remindAt.toZonedDateTime(),
    host = host,
    isUserEventCreator = isUserEventCreator
)

fun Event.toEventRequest() = CreateEventRequest(
    id = id,
    title = title,
    description = description,
    from = time.toInstant().toEpochMilli(),
    to = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli()
)

fun Event.toEventEntity() = EventEntity(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    timeEnd = time.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli(),
    host = host,
    isUserEventCreator = isUserEventCreator
)

fun EventEntity.toEvent() = Event(
    id = id,
    title = title,
    description = description?:"",
    time = time.toZonedDateTime(),
    endTime = timeEnd.toZonedDateTime(),
    remindAt = remindAt.toZonedDateTime(),
    host = host,
    isUserEventCreator = isUserEventCreator
)