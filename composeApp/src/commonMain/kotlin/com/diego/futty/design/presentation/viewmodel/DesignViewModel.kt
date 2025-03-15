package com.diego.futty.design.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.diego.futty.design.presentation.component.banner.BannerUIData

class DesignViewModel : DesignViewContract, ViewModel() {
    private val _buttonEnabled = mutableStateOf(true)
    override val buttonEnabled: State<Boolean> = _buttonEnabled


    fun setup() {
        // setup
    }

    override fun onButtonPressed() {
        _buttonEnabled.value = _buttonEnabled.value.not()
    }

    override fun getScrollableBanners() = BannerUIData(
        title = "Scrollable Banner",
        description = "Éste banner puede varias cosas en un mismo lugar. Máximo 2 líneas",
        labelAction = "Ver más",
    )
}
