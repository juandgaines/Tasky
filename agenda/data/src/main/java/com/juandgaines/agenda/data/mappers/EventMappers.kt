package com.juandgaines.agenda.data.mappers

import com.juandgaines.agenda.data.event.remote.AttendeeDto
import com.juandgaines.agenda.data.event.remote.AttendeeMinimalDto
import com.juandgaines.agenda.data.event.remote.CreateEventRequest
import com.juandgaines.agenda.data.event.remote.EventResponse
import com.juandgaines.agenda.data.event.remote.UpdateEventRequest
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.Attendee
import com.juandgaines.agenda.domain.agenda.AttendeeMinimal
import com.juandgaines.agenda.domain.agenda.IAttendee
import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.core.data.database.event.AttendeeEntity
import com.juandgaines.core.data.database.event.EventEntity
import java.time.ZonedDateTime

fun EventResponse.toEvent() = Event(
    id = id,
    title = title,
    description = description,
    time = from.toZonedDateTime(),
    endTime = to.toZonedDateTime(),
    remindAt = remindAt.toZonedDateTime(),
    host = host,
    isUserEventCreator = isUserEventCreator,
    attendee = attendees.map { it.toAttendee(host) }
)

fun AttendeeDto.toAttendee(id:String) = Attendee(
    email = email,
    fullName = fullName,
    userId = userId,
    eventId = eventId,
    isGoing = isGoing,
    remindAt = remindAt.toZonedDateTime(),
    isUserCreator = id == userId,
)

fun Event.toEventRequest() = CreateEventRequest(
    id = id,
    title = title,
    description = description,
    from = time.toInstant().toEpochMilli(),
    to = endTime.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli()
)

fun Event.toUpdateEventRequest() = UpdateEventRequest(
    id = id,
    title = title,
    description = description,
    from = time.toInstant().toEpochMilli(),
    to = endTime.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli(),
    attendeeIds = attendee.filter { it.isUserCreator }.map { it.userId },
    deletedPhotoKeys = emptyList(),
    isGoing = false // TODO: change with implementation of attendees
)

fun Event.toEventEntity() = EventEntity(
    id = id,
    title = title,
    description = description,
    time = time.toInstant().toEpochMilli(),
    timeEnd = endTime.toInstant().toEpochMilli(),
    remindAt = remindAt.toInstant().toEpochMilli(),
    host = host,
    isUserEventCreator = isUserEventCreator,
    attendees = attendee.map { it.toAttendeeEntity() }
)

fun EventEntity.toEvent() = Event(
    id = id,
    title = title,
    description = description?:"",
    time = time.toZonedDateTime(),
    endTime = timeEnd.toZonedDateTime(),
    remindAt = remindAt.toZonedDateTime(),
    host = host,
    isUserEventCreator = isUserEventCreator,
    attendee = attendees.map { it.toAttendee() }
)

fun AttendeeEntity.toAttendee() = Attendee(
    email = email,
    fullName = fullName,
    userId = userId,
    eventId = eventId,
    isGoing = isGoing,
    remindAt = remindAt.toZonedDateTime(),
    isUserCreator = isUserCreator,
)

fun Attendee.toAttendeeEntity() = AttendeeEntity(
    email = email,
    fullName = fullName,
    userId = userId,
    eventId = eventId,
    isGoing = isGoing,
    remindAt = remindAt.toInstant().toEpochMilli(),
    isUserCreator = isUserCreator
)

fun IAttendee.toAttendeeEntity() = AttendeeEntity(
    email = email,
    fullName = fullName,
    userId = userId,
    eventId = "",
    isGoing = isGoing,
    remindAt = ZonedDateTime.now().toInstant().toEpochMilli(),
    isUserCreator = isUserCreator
)

fun AttendeeMinimalDto.toMinimalAttendee() = AttendeeMinimal(
    email = email,
    fullName = fullName,
    userId = userId,
    isGoing = true,
    isUserCreator = false
)

