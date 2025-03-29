package com.diego.futty.core.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class UserPreferences(private val settings: Settings) {
    fun saveUserId(username: String) {
        settings[USER_ID] = username
    }

    fun getUserId(): String? {
        return settings[USER_ID]
    }

    fun saveUserEmail(email: String) {
        settings[USER_EMAIL] = email
    }

    fun getUserEmail(): String? {
        return settings[USER_EMAIL]
    }

    fun saveDarkMode(enabled: Boolean) {
        settings[DARK_MODE] = enabled
    }

    fun isDarkModeEnabled(): Boolean? {
        return settings[DARK_MODE]
    }

    companion object {
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val DARK_MODE = "dark_mode"
    }
}

expect fun provideSettings(): Settings
