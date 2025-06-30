package com.diego.futty.core.presentation.utils.strings

object StringsEn : Strings {
    override fun hello() = "Hello"
    override fun postDate(day: Int, month: String, year: Int) = "$month $day, $year"
    override fun minutesAgo(mins: Long) = "$mins minutes ago"
}
