package com.diego.futty.setup.profile.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.ImageBitmap
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.design.presentation.component.chip.ChipModel
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras

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
    val posts: State<List<PostWithExtras>?>
    val postsCant: State<Int?>
    val followersCant: State<Int?>
    val followsCant: State<Int?>
    val showFollowButton: State<Boolean>
    val followingUser: State<Boolean>
    val openedPost: State<PostWithExtras?>
    val isRefreshing: State<Boolean>
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
    fun onPostClicked(post: PostWithExtras)
    fun onLikeClicked(post: PostWithExtras, fromDetail: Boolean)
    fun fetchOwnFeed()
    fun onFeedRefreshed()
}
