package com.diego.futty.setup.view

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State

interface SetupViewContract {
    val palette: State<ColorScheme>
    val currentRoute: State<SetupRoute>
    fun updateRoute(newRoute: SetupRoute)
}
