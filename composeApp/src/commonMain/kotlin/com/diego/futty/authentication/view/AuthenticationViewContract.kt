package com.diego.futty.authentication.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State

interface AuthenticationViewContract {
    val currentRoute: State<AuthenticationRoute>
    fun updateRoute(newRoute: AuthenticationRoute)
    val palette: State<ColorScheme>
}
