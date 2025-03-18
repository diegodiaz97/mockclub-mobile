package com.diego.futty.core.data.local

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var appContext: Context

actual fun provideSettings(): Settings {
    val sharedPreferences = appContext.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}
