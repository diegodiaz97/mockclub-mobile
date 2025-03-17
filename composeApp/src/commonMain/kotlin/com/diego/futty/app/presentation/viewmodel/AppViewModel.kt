package com.diego.futty.app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.app.Route

class AppViewModel : AppViewContract, ViewModel() {
    private val _currentRoute = mutableStateOf<Route>(Route.Design)
    override val currentRoute: State<Route> = _currentRoute

    fun setup() {
        // setup
    }

    override fun updateRoute(newRoute: Route) {
        _currentRoute.value = newRoute
    }
}
