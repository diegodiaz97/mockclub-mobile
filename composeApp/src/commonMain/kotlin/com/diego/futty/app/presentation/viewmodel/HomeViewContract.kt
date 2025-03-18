package com.diego.futty.app.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.app.presentation.view.HomeRoute

interface HomeViewContract {
    val currentRoute: State<HomeRoute>
    fun updateRoute(newRoute: HomeRoute)
}
