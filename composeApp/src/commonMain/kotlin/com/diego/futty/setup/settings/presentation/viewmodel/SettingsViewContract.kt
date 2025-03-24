package com.diego.futty.setup.settings.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.vector.ImageVector
import com.diego.futty.home.design.presentation.component.banner.Banner

interface SettingsViewContract {
    val palette: State<ColorScheme>
    val topBarIcon: State<ImageVector>
    val banner: State<Banner?>
    fun onChangeThemeClicked()
    fun onLogoutClicked()
    fun onBackClicked()
}
