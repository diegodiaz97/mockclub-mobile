package com.diego.futty.setup.profile.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.Chip.ChipModel
import com.diego.futty.home.feed.domain.model.User

interface ProfileViewContract {
    val user: State<User?>
    val chipItems: State<List<ChipModel>?>
    val selectedChips: State<List<ChipModel>>
    fun onBackClicked()
    fun onSettingsClicked()
    fun onChipSelected(chip: ChipModel)
}
