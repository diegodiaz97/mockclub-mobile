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
    val openedImage: State<String?>
    val isRefreshing: State<Boolean>
    val modal: State<Modal?>
    val showPostCreation: State<Boolean>
    val postMaxLength: State<Int>
    val text: State<String>
    val team: State<String>
    val brand: State<String>
    val images: State<List<ByteArray>>
    fun startReveal(revealState: RevealState)
    fun onProfileClicked()
    fun onImageClicked(image: String)
    fun onImageClosed()
    fun onPostClicked(post: PostWithUser)
    fun onPostClosed()
    fun createPost()
    fun showPostCreation()
    fun dismissPostCreation()
    fun fetchFeed()
    fun updateText(newText: String)
    fun updateTeam(newTeam: String)
    fun updateBrand(newBrand: String)
    fun onFeedRefreshed()
}
