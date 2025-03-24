package com.diego.futty.core.presentation.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.diego.futty.core.presentation.theme.DayColorScheme

@Composable
actual fun SetStatusBarColor(color: Color) {
    SetSystemBarsColor(
        statusBarColor = color,
        navigationBarColor = color,
        isSystemDarkIcons = MaterialTheme.colorScheme == DayColorScheme
    )
}
