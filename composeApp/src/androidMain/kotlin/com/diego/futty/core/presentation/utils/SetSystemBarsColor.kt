package com.diego.futty.core.presentation.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetSystemBarsColor(
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    isSystemDarkIcons: Boolean = false,
    isStatusBarDarkIcons: Boolean = false,
    isNavigationBarDarkIcons: Boolean = false,
) {
    val view = LocalView.current
    if (view.isInEditMode.not()) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.statusBarColor = statusBarColor.toArgb()
            window?.navigationBarColor = navigationBarColor.toArgb()

            val insetsController = window?.let { window ->
                WindowCompat.getInsetsController(window, view)
            }

            when {
                isSystemDarkIcons -> {
                    insetsController?.isAppearanceLightStatusBars = isSystemDarkIcons
                    insetsController?.isAppearanceLightNavigationBars = isSystemDarkIcons
                }
                else -> {
                    insetsController?.isAppearanceLightStatusBars = isStatusBarDarkIcons
                    insetsController?.isAppearanceLightNavigationBars = isNavigationBarDarkIcons
                }
            }
        }
    }
}
