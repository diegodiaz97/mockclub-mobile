package com.diego.futty.app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.app.presentation.view.HomeRoute

class HomeViewModel : HomeViewContract, ViewModel() {
    private val _currentRoute = mutableStateOf<HomeRoute>(HomeRoute.Design)
    override val currentRoute: State<HomeRoute> = _currentRoute

    fun setup() {
        // setup
    }

    override fun updateRoute(newRoute: HomeRoute) {
        _currentRoute.value = newRoute
    }
}
