package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.feed.domain.model.ActionableImage
import com.diego.futty.home.feed.domain.model.Post
import com.diego.futty.home.feed.domain.model.User

interface FeedViewContract {
    val user: State<User?>
    val posts: State<List<Post>?>
    val openedPost: State<Post?>
    val openedImage: State<ActionableImage?>
    fun onProfileClicked()
    fun onImageClicked(actionableImage: ActionableImage)
    fun onImageClosed()
    fun onPostClicked(post: Post)
    fun onPostClosed()
}
