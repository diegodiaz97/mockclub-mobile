package com.diego.futty.design.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.design.presentation.component.banner.BannerUIData

interface DesignViewContract {
    val buttonEnabled: State<Boolean>
    fun onButtonPressed()
    fun getScrollableBanners(): BannerUIData
}
