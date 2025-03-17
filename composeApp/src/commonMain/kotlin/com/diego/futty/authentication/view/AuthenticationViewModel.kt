package com.diego.futty.authentication.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.core.presentation.theme.DayColorScheme

class AuthenticationViewModel : AuthenticationViewContract, ViewModel() {
    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _currentRoute = mutableStateOf<AuthenticationRoute>(AuthenticationRoute.Login)
    override val currentRoute: State<AuthenticationRoute> = _currentRoute

    fun setup() {
        // setup
    }

    override fun updateRoute(newRoute: AuthenticationRoute) {
        _currentRoute.value = newRoute
    }
}
