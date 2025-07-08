package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.post.domain.model.PostWithUser
import com.svenjacobs.reveal.RevealState

interface FeedViewContract {
    val user: State<User?>
    val posts: State<List<PostWithUser>?>
    val openedPost: State<PostWithUser?>
    val openedImage: State<List<String>>
    val openedImageIndex: State<Int>
    val isRefreshing: State<Boolean>
    val modal: State<Modal?>
    val postCreationProgress: State<Float>
    fun startReveal(revealState: RevealState)
    fun onProfileClicked()
    fun onImageClicked(images: List<String>, index: Int)
    fun onImageClosed()
    fun onPostClicked(post: PostWithUser)
    fun onLikeClicked(post: PostWithUser, fromDetail: Boolean = false)
    fun onPostCreated()
    fun onStartPostCreation()
    fun showPostCreation()
    fun fetchFeed()
    fun onFeedRefreshed()
}
