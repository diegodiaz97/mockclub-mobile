package com.diego.futty.profile.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State

interface ProfileViewContract {
    val palette: State<ColorScheme>
    fun onBackClicked()
    fun onChangeThemeClicked()
}
