package com.juandgaines.agenda.domain.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun Long.toUtcZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}

fun ZonedDateTime.toZonedDateTimeWithZoneId(zone: ZoneId): ZonedDateTime {
    return this.withZoneSameLocal(zone)
}

fun ZonedDateTime.toUtcZonedDateTime(): ZonedDateTime {
    return this.withZoneSameInstant(ZoneOffset.UTC)
}