package com.diego.futty.core.presentation.utils.strings

object StringsEs : Strings {
    override fun hello() = "Hola"
    override fun postDate(day: Int, month: String, year: Int) = "$day de $month de $year"
    override fun minutesAgo(mins: Long) = "Hace $mins minutos"
}
