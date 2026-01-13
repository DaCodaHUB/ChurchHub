package com.dangle.churchhub.core.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun parseIsoInstantToEpochMs(iso: String): Long =
    Instant.parse(iso).toEpochMilli()

fun parseLocalDateToEpochMs(date: String): Long {
    // "YYYY-MM-DD"
    val localDate = LocalDate.parse(date)
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
