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

    fun saveUserType(type: String) {
        settings[USER_TYPE] = type
    }

    fun getUserType(): String? {
        return settings[USER_TYPE]
    }

    fun saveDarkMode(enabled: Boolean) {
        settings[DARK_MODE] = enabled
    }

    fun isDarkModeEnabled(): Boolean? {
        return settings[DARK_MODE]
    }

    fun saveOnboarding(done: Boolean) {
        settings[ONBOARDING] = done
    }

    fun getOnboarding(): Boolean? {
        return settings[ONBOARDING]
    }

    companion object {
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_TYPE = "user_type"
        const val DARK_MODE = "dark_mode"
        const val ONBOARDING = "onboarding"
    }
}

expect fun provideSettings(): Settings
