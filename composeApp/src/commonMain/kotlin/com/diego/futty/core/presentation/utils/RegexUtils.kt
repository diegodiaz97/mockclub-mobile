package com.diego.futty.core.presentation.utils

object RegexUtils {
    val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@\$!%?&#^*_/]{8,}$")
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
}
