package com.diego.futty.home.design.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.Chip.ChipModel
import com.diego.futty.home.design.presentation.component.banner.BannerUIData

interface DesignViewContract {
    val chipItems: State<List<ChipModel>>
    val buttonText: State<String>
    val buttonEnabled: State<Boolean>
    fun onButtonClicked()
    fun getScrollableBanners(): BannerUIData
    fun onScrollBannerClicked(text: String)
    fun onProfileClicked()
    fun onBottomSheetDismissed()
    val bottomsheetDismissed: State<Boolean>
    val selectedChip: State<Int>
    fun onChipSelected(index: Int)
}
