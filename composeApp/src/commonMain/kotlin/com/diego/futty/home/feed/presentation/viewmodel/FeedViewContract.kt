package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithUser
import com.svenjacobs.reveal.RevealState

interface FeedViewContract {
    val user: State<User?>
    val posts: State<List<PostWithUser>?>
    val openedPost: State<PostWithUser?>
    val openedImage: State<List<String>>
    val openedImageIndex: State<Int>
    val openedImageRatio: State<Float>
    val isRefreshing: State<Boolean>
    val postCreationProgress: State<Float>
    val likedPostIds: State<Set<String>>
    val modal: State<Modal?>
    fun startReveal(revealState: RevealState)
    fun onProfileClicked()
    fun onImageClicked(images: List<String>, index: Int, ratio: Float)
    fun onImageClosed()
    fun onPostClicked(post: PostWithUser)
    fun onLikeClicked(post: PostWithUser, fromDetail: Boolean = false)
    fun onPostCreated()
    fun onStartPostCreation()
    fun showPostCreation()
    fun updateLikes(likes: Set<String>)
    fun fetchFeed()
    fun onFeedRefreshed()
    fun onChallengesClicked()
}
