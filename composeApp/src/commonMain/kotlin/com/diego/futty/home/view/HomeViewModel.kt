package com.diego.futty.home.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme

class HomeViewModel(
    private val preferences: UserPreferences,
) : HomeViewContract, ViewModel() {

    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _currentRoute = mutableStateOf<HomeRoute>(HomeRoute.Feed)
    override val currentRoute: State<HomeRoute> = _currentRoute

    private val _showBottomBar = mutableStateOf(true)
    override val showBottomBar: State<Boolean> = _showBottomBar

    fun setup() {
        updatePalette()
    }

    override fun updateRoute(newRoute: HomeRoute) {
        updatePalette()
        _currentRoute.value = newRoute
        updateBottomBar()
    }

    private fun updatePalette() {
        _palette.value = if (preferences.isDarkModeEnabled() == true) {
            NightColorScheme
        } else {
            DayColorScheme
        }
    }

    private fun updateBottomBar() {
        _showBottomBar.value = when (_currentRoute.value) {
            HomeRoute.Setup -> false
            else -> true
        }
    }
}
