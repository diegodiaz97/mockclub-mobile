package com.diego.futty.profile.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme

class ProfileViewModel(
    private val preferences: UserPreferences,
) : ProfileViewContract, ViewModel() {

    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private var _navigate: (AuthenticationRoute) -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(
        navController: NavHostController,
    ) {
        _navigate = { navController.navigate(it) }
        _back = { navController.popBackStack() }
    }

    override fun onBackClicked() {
        _back()
    }

    override fun onChangeThemeClicked() {
        _palette.value = if (_palette.value == DayColorScheme) {
            NightColorScheme
        } else {
            DayColorScheme
        }

        val currentlyDark = preferences.isDarkModeEnabled()?.not() ?: true
        preferences.saveDarkMode(currentlyDark)
    }
}
