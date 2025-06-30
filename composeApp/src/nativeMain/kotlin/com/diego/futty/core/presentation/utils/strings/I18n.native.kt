package com.diego.futty.core.presentation.utils.strings

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getCurrentLanguage(): String = NSLocale.currentLocale.languageCode
