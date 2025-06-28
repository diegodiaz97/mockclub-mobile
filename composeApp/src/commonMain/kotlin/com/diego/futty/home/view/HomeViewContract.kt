package com.diego.futty.home.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State

interface HomeViewContract {
    val palette: State<ColorScheme>
    val currentRoute: State<HomeRoute>
    val showBottomBar: State<Boolean>
    val canUseBottomBar: State<Boolean>
    fun updateRoute(newRoute: HomeRoute)
}
