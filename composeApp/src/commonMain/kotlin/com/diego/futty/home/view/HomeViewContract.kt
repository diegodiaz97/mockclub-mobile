package com.diego.futty.home.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import com.diego.futty.app.presentation.view.HomeRoute

interface HomeViewContract {
    val palette: State<ColorScheme>
    val currentRoute: State<HomeRoute>
    fun updateRoute(newRoute: HomeRoute)
    val showBottomBar: State<Boolean>
}
