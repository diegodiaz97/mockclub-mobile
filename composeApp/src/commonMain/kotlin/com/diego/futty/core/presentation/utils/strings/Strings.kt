package com.diego.futty.core.presentation.utils.strings

interface Strings {
    fun hello(): String
    fun postDate(day: Int, month: String, year: Int): String
    fun minutesAgo(mins: Long): String
    // más textos que uses
}