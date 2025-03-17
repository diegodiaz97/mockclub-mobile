package com.diego.futty.app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.app.AppRoute

class AppViewModel : AppViewContract, ViewModel() {
    private val _currentRoute = mutableStateOf<AppRoute>(AppRoute.Design)
    override val currentRoute: State<AppRoute> = _currentRoute

    fun setup() {
        // setup
    }

    override fun updateRoute(newRoute: AppRoute) {
        _currentRoute.value = newRoute
    }
}
