package com.juandgaines.agenda.domain.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Long.toUtcZonedDateTime(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}

fun LocalDate.toLocalDateWithZoneId(zone: ZoneId): LocalDate {
    return this.atStartOfDay(zone).toLocalDate()
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