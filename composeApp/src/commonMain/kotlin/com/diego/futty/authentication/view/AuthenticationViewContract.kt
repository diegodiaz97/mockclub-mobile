package com.diego.futty.authentication.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State

interface AuthenticationViewContract {
    val palette: State<ColorScheme>
    val currentRoute: State<AuthenticationRoute>
    fun updateRoute(newRoute: AuthenticationRoute)
}
