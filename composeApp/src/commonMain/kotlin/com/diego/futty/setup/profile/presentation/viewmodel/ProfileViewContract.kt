package com.diego.futty.setup.profile.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.ImageBitmap
import com.diego.futty.home.design.presentation.component.Chip.ChipModel
import com.diego.futty.home.feed.domain.model.User

interface ProfileViewContract {
    val showUpdateImage: State<Boolean>
    val user: State<User?>
    val urlImage: State<String?>
    val initials: State<String?>
    val chipItems: State<List<ChipModel>?>
    val selectedChips: State<List<ChipModel>>
    fun onBackClicked()
    fun onSettingsClicked()
    fun onChipSelected(chip: ChipModel)
    fun updateImage(imageBitmap: ImageBitmap, imageByteArray: ByteArray)
    fun showUpdateImage(show: Boolean)
}
