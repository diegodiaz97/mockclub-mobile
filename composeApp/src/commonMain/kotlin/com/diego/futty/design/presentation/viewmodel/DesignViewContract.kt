package com.diego.futty.design.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import com.diego.futty.design.presentation.component.banner.BannerUIData

interface DesignViewContract {
    val palette: State<ColorScheme>
    val buttonText: State<String>
    val buttonEnabled: State<Boolean>
    fun onButtonPressed()
    fun getScrollableBanners(): BannerUIData
    fun onScrollBannerPressed(text: String)
    fun onProfilePressed()
    fun onBottomSheetDismissed()
    val bottomsheetDismissed: State<Boolean>
    val selectedChip: State<Int>
    fun onChipSelected(index: Int)
}
