package com.androidforge.streakflow.core.common.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.toLocalDate(): LocalDate {
    return LocalDate.ofInstant(this, ZoneId.systemDefault())
}

fun LocalDate.toInstant(): Instant {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant()
}

fun LocalTime.formatTime(): String {
    return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
}

fun LocalDate.isToday(): Boolean {
    return this == LocalDate.now()
}

fun LocalDate.isYesterday(): Boolean {
    return this == LocalDate.now().minusDays(1)
}

fun LocalDate.isBeforeOrEqualTo(other: LocalDate): Boolean {
    return !this.isAfter(other)
}

fun LocalDate.isAfterOrEqualTo(other: LocalDate): Boolean {
    return !this.isBefore(other)
}