package com.diego.futty.design.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.design.presentation.component.Chip.ChipModel
import com.diego.futty.design.presentation.component.banner.BannerUIData

interface DesignViewContract {
    val chipItems: State<List<ChipModel>>
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
