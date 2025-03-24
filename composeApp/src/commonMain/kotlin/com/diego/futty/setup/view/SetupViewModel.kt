package com.diego.futty.setup.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.core.presentation.theme.DayColorScheme

class SetupViewModel : SetupViewContract, ViewModel() {
    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _currentRoute = mutableStateOf<SetupRoute>(SetupRoute.Profile)
    override val currentRoute: State<SetupRoute> = _currentRoute

    fun setup() {
        // setup
    }

    override fun updateRoute(newRoute: SetupRoute) {
        _currentRoute.value = newRoute
    }
}
