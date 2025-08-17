package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.svenjacobs.reveal.RevealState

interface FeedViewContract {
    val user: State<User?>
    val posts: State<List<PostWithExtras>?>
    val openedPost: State<PostWithExtras?>
    val openedImage: State<List<String>>
    val openedImageIndex: State<Int>
    val openedImageRatio: State<Float>
    val isRefreshing: State<Boolean>
    val postCreationProgress: State<Float>
    val clickedUser: State<String>
    val modal: State<Modal?>
    fun startReveal(revealState: RevealState)
    fun onProfileClicked()
    fun onImageClicked(images: List<String>, index: Int, ratio: Float)
    fun onImageClosed()
    fun onPostClicked(post: PostWithExtras)
    fun onLikeClicked(post: PostWithExtras, fromDetail: Boolean = false)
    fun onPostCreated()
    fun onStartPostCreation()
    fun showPostCreation()
    fun updateLikes(userMadeLike: Boolean)
    fun fetchFeed()
    fun onFeedRefreshed()
    fun onChallengesClicked()
    fun onUserClicked(user: User)
    fun resetUserId()
}
