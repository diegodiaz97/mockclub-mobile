package com.diego.futty.app.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.app.Route

interface AppViewContract {
    val currentRoute: State<Route>
    fun updateRoute(newRoute: Route)
}
