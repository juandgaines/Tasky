package com.juandgaines.agenda.domain.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Long.toUtcZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}

fun ZonedDateTime.toZonedDateTimeWithZoneId(zone: ZoneId): ZonedDateTime {
    return this.withZoneSameLocal(zone)
}

fun ZonedDateTime.toUtcZonedDateTime(): ZonedDateTime {
    return this.withZoneSameInstant(ZoneOffset.UTC)
}

fun ZonedDateTime.toFormattedDate(): String {
    DateTimeFormatter.ofPattern("dd MMMM yyyy").let {
        return this.format(it)
    }
}