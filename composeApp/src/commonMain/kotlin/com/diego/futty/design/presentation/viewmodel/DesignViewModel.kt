package com.diego.futty.design.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme
import com.diego.futty.design.presentation.component.banner.BannerUIData

class DesignViewModel : DesignViewContract, ViewModel() {
    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _buttonEnabled = mutableStateOf(true)
    override val buttonEnabled: State<Boolean> = _buttonEnabled

    private val _buttonText = mutableStateOf("Continuar")
    override val buttonText: State<String> = _buttonText

    fun setup() {
        // setup
    }

    override fun onButtonPressed() {
        _buttonEnabled.value = _buttonEnabled.value.not()
    }

    override fun onProfilePressed() {
        _palette.value = if (_palette.value == DayColorScheme) {
            NightColorScheme
        } else {
            DayColorScheme
        }
    }

    override fun getScrollableBanners() = BannerUIData(
        title = "Scrollable Banner",
        description = "Éste banner puede varias cosas en un mismo lugar. Máximo 2 líneas",
        labelAction = "Ver más",
        action = { onScrollBannerPressed("accion 1") },
    )

    override fun onScrollBannerPressed(text: String) {
        _buttonText.value = text
    }
}
