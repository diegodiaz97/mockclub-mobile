package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.ActionableImage
import com.diego.futty.home.feed.domain.model.Post
import com.diego.futty.home.feed.domain.model.User
import com.svenjacobs.reveal.RevealState

interface FeedViewContract {
    val user: State<User?>
    val posts: State<List<Post>?>
    val openedPost: State<Post?>
    val openedImage: State<ActionableImage?>
    val modal: State<Modal?>
    fun startReveal(revealState: RevealState)
    fun onProfileClicked()
    fun onImageClicked(actionableImage: ActionableImage)
    fun onImageClosed()
    fun onPostClicked(post: Post)
    fun onPostClosed()
}
