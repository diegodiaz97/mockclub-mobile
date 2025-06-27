package com.diego.futty.setup.profile.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.ImageBitmap
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.design.presentation.component.chip.ChipModel
import com.diego.futty.home.feed.domain.model.User

interface ProfileViewContract {
    val showUpdateImage: State<Boolean>
    val user: State<User?>
    val userId: State<String>
    val urlImage: State<String?>
    val initials: State<String?>
    val launchGallery: State<Boolean>
    val launchCamera: State<Boolean>
    val chipItems: State<List<ChipModel>?>
    val selectedChips: State<List<ChipModel>>
    val postsCant: State<Int?>
    val followersCant: State<Int?>
    val followsCant: State<Int?>
    val showFollowButton: State<Boolean>
    val followingUser: State<Boolean>
    val modal: State<Modal?>
    fun onBackClicked()
    fun onSettingsClicked()
    fun onChipSelected(chip: ChipModel)
    fun updateImage(imageBitmap: ImageBitmap, imageByteArray: ByteArray)
    fun showUpdateImage()
    fun launchGallery()
    fun launchCamera()
    fun onEditClicked()
    fun onVerifyClicked()
    fun obtainFollowers()
    fun obtainFollows()
    fun onFollowOrUnfollowClicked()
}
