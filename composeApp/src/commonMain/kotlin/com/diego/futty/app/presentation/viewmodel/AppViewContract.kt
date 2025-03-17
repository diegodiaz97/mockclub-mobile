package com.diego.futty.app.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.app.AppRoute

interface AppViewContract {
    val currentRoute: State<AppRoute>
    fun updateRoute(newRoute: AppRoute)
}
