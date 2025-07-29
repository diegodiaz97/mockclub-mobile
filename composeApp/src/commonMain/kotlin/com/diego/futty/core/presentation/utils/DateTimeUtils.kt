package com.diego.futty.core.presentation.utils

import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.data.local.provideSettings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.getFullMonthLabel(): String {
    val past = Instant.fromEpochMilliseconds(this)
    val pastDateTime = past.toLocalDateTime(TimeZone.currentSystemDefault())

    return "${pastDateTime.dayOfMonth} de ${pastDateTime.month.getMonthName()} de ${pastDateTime.year}"
}

fun Long.getTimeLabel(): String {
    val past = Instant.fromEpochMilliseconds(this)
    val pastDateTime = past.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour = pastDateTime.time.hour.toString().padStart(2, '0')
    val minute = pastDateTime.time.minute.toString().padStart(2, '0')
    return "$hour:$minute"
}

fun Long.getTimeAgoLabel(): String {
    val preferences = UserPreferences(provideSettings())
    val delta = preferences.getServerTimeDelta()
    val nowMillis = Clock.System.now().toEpochMilliseconds() + (delta ?: 0L)

    val pastInstant = Instant.fromEpochMilliseconds(this)
    val nowInstant =  Instant.fromEpochMilliseconds(nowMillis)

    val duration = nowInstant - pastInstant
    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        minutes < 1 -> "Hace menos de un minuto"
        minutes < 60 -> "Hace $minutes ${if (minutes == 1L) "minuto" else "minutos"}"
        hours < 24 -> "Hace $hours ${if (hours == 1L) "hora" else "horas"}"
        days == 1L -> {
            val time = pastInstant.toLocalDateTime(TimeZone.currentSystemDefault()).time
            "Ayer a las ${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
        }
        days < 7 -> "Hace $days días"
        days < 30 -> "Hace ${days / 7} semanas"
        days < 365 -> "Hace ${days / 30} meses"
        else -> "Hace ${days / 365} años"
    }
}

fun Long.getShortTimeAgoLabel(): String {
    val preferences = UserPreferences(provideSettings())
    val delta = preferences.getServerTimeDelta()
    val nowMillis = Clock.System.now().toEpochMilliseconds() + (delta ?: 0L)

    val pastInstant = Instant.fromEpochMilliseconds(this)
    val nowInstant =  Instant.fromEpochMilliseconds(nowMillis)

    val duration = nowInstant - pastInstant
    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        minutes < 1 -> "Ahora"
        minutes < 60 -> "$minutes min"
        hours < 24 -> "$hours h"
        days < 7 -> "$days d"
        days < 30 -> "${days / 7} semanas"
        days < 365 -> "${days / 30} meses"
        else -> "${days / 365} años"
    }
}

fun Month.getMonthName(): String = when (this) {
    Month.JANUARY -> "enero"
    Month.FEBRUARY -> "febrero"
    Month.MARCH -> "marzo"
    Month.APRIL -> "abril"
    Month.MAY -> "mayo"
    Month.JUNE -> "junio"
    Month.JULY -> "julio"
    Month.AUGUST -> "agosto"
    Month.SEPTEMBER -> "septiembre"
    Month.OCTOBER -> "octubre"
    Month.NOVEMBER -> "noviembre"
    Month.DECEMBER -> "diciembre"
    else -> ""
}
