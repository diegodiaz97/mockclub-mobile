package com.diego.futty.core.presentation.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
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
    val now = Clock.System.now()
    val past = Instant.fromEpochMilliseconds(this)

    val nowDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    val pastDateTime = past.toLocalDateTime(TimeZone.currentSystemDefault())

    val totalMinutes = (now.epochSeconds - past.epochSeconds) / 60
    val totalHours = totalMinutes / 60
    val totalDays = pastDateTime.date.daysUntil(nowDateTime.date)
    val totalMonths = nowDateTime.date.monthNumber - pastDateTime.date.monthNumber +
            (nowDateTime.date.year - pastDateTime.date.year) * 12
    val totalYears = nowDateTime.date.year - pastDateTime.date.year

    return when {
        totalMinutes < 1 -> "Hace menos de un minuto"
        totalMinutes < 60 -> "Hace $totalMinutes ${if (totalMinutes == 1L) "minuto" else "minutos"}"
        totalHours < 24 -> "Hace $totalHours ${if (totalHours == 1L) "hora" else "horas"}"
        totalDays == 1 -> "Ayer a las ${
            pastDateTime.time.hour.toString().padStart(2, '0')
        }:${pastDateTime.time.minute.toString().padStart(2, '0')}"

        totalDays in 2..6 -> "Hace $totalDays días"
        totalDays in 7..27 -> "Hace ${totalDays / 7} semanas"
        totalMonths in 1..11 -> "Hace $totalMonths ${if (totalMonths == 1) "mes" else "meses"}"
        totalYears >= 1 -> "Hace $totalYears ${if (totalYears == 1) "año" else "años"}"
        else -> "Hace un momento"
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
