package com.diego.futty.authentication.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme

class AuthenticationViewModel(
    private val preferences: UserPreferences,
) : AuthenticationViewContract, ViewModel() {
    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _currentRoute = mutableStateOf<AuthenticationRoute>(AuthenticationRoute.Welcome)
    override val currentRoute: State<AuthenticationRoute> = _currentRoute

    fun setup() {
        // setup
    }

    override fun updateRoute(newRoute: AuthenticationRoute) {
        updatePalette()
        _currentRoute.value = newRoute
    }

    private fun updatePalette() {
        _palette.value = if (preferences.isDarkModeEnabled() == true) {
            NightColorScheme
        } else {
            DayColorScheme
        }
    }
}
