package com.juandgaines.agenda.domain.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Long.toUtcLocalDateTime(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}

fun ZonedDateTime.isToday(): Boolean {
    return this.toLocalDate() == LocalDate.now()
}

fun LocalDate.toLocalDateWithZoneId(zone: ZoneId): LocalDate {
    return this.atStartOfDay(zone).toLocalDate()
}

fun LocalDate.toZonedDateTime(
    localTime: LocalTime = LocalTime.MIDNIGHT,
    zoneId: ZoneId = ZoneId.systemDefault()
): ZonedDateTime {
    return this.atTime(localTime)
        .atZone(zoneId)
}

fun LocalDate.toUtcLocalDate(): LocalDate {
    val localTime= LocalTime.NOON
    val zoneId = ZoneId.systemDefault()
    val localZonedDateTime = this.atTime(localTime).atZone(zoneId).toLocalDate()
    return localZonedDateTime.toLocalDateWithZoneId(ZoneId.of("UTC"))
}

fun LocalDate.toEpochMilliUtc(): Long {
    return this.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
}
fun LocalDate.toEpochMilli(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun LocalDate.toFormattedDate(): String {
    DateTimeFormatter.ofPattern("dd MMMM yyyy").let {
        return this.format(it)
    }
}

fun LocalDate.startOfDay(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun LocalDate.endOfDay(): Long {
    return this.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}


fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return Instant.ofEpochMilli(this) // Convert Long to Instant
        .atZone(zoneId) // Apply the desired ZoneId to get ZonedDateTime
}


fun Long.toUtcZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}

fun ZonedDateTime.toZonedDateTimeWithZoneId(zone: ZoneId): ZonedDateTime {
    return this.withZoneSameLocal(zone)
}

fun ZonedDateTime.toUtcFromZonedDateTime(): ZonedDateTime {
    return this.withZoneSameInstant(ZoneOffset.UTC)
}
//from zoneddatetime to epoch millis
fun ZonedDateTime.toEpochMilli(): Long {
    return this.toInstant().toEpochMilli()
}

fun ZonedDateTime.toFormattedSingleDateTime(): String {
    DateTimeFormatter.ofPattern("MMM dd, HH:mm").let {
        return this.format(it)
    }
}

fun ZonedDateTime.toFormattedTime(): String {
    DateTimeFormatter.ofPattern("HH:mm").let {
        return this.format(it)
    }
}

fun ZonedDateTime.toFormattedDate(): String {
    DateTimeFormatter.ofPattern("MMM dd yyyy").let {
        return this.format(it)
    }
}

