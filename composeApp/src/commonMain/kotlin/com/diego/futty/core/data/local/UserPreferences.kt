package com.diego.futty.core.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class UserPreferences(private val settings: Settings) {

    fun saveUsername(username: String) {
        settings[USERNAME] = username
    }

    fun getUsername(): String? {
        return settings[USERNAME]
    }

    fun saveDarkMode(enabled: Boolean) {
        settings[DARK_MODE] = enabled
    }

    fun isDarkModeEnabled(): Boolean {
        return settings[DARK_MODE] ?: false
    }

    fun updateNewUser(enabled: Boolean) {
        settings[DARK_MODE] = enabled
    }

    fun isNewUser(): Boolean {
        return settings[DARK_MODE] ?: false
    }

    companion object {
        const val USERNAME = "username"
        const val DARK_MODE = "dark_mode"
        const val NEW_USER = "new_user"
    }
}

expect fun provideSettings(): Settings
