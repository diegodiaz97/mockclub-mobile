package com.diego.futty.core.presentation.utils.strings

/** USAGE SAMPLE
 *
 * val text = I18n.strings.hello()
 * val date = I18n.strings.postDate(27, "junio", 2025)
 *
 * */

object I18n {
    val strings: Strings
        get() = when (getCurrentLanguage()) {
            "es" -> StringsEs
            else -> StringsEn
        }
}

expect fun getCurrentLanguage(): String
