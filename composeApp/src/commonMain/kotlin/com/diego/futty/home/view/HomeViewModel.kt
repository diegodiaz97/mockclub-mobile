package com.diego.futty.home.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val preferences: UserPreferences,
) : HomeViewContract, ViewModel() {

    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _currentRoute = mutableStateOf<HomeRoute>(HomeRoute.Feed)
    override val currentRoute: State<HomeRoute> = _currentRoute

    private val _showBottomBar = mutableStateOf(false)
    override val showBottomBar: State<Boolean> = _showBottomBar

    private val _canUseBottomBar = mutableStateOf(false)
    override val canUseBottomBar: State<Boolean> = _canUseBottomBar

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

    private fun updateBottomBar() = viewModelScope.launch {
        _showBottomBar.value = when (_currentRoute.value) {
            HomeRoute.Setup, HomeRoute.CreatePost -> false
            else -> true
        }
        delay(300)
        _canUseBottomBar.value = _showBottomBar.value
    }
}
