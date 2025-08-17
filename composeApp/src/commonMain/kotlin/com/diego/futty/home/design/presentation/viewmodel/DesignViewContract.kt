package com.diego.futty.home.design.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import kotlinx.coroutines.flow.StateFlow

interface DesignViewContract {
    val searchTypes: State<List<String>>
    val selectedSearchType: State<Int>
    val searchText: StateFlow<String>
    val searchUsers: StateFlow<List<User>?>
    val searchPosts: StateFlow<List<PostWithExtras>?>
    val openedPost: State<PostWithExtras?>
    val clickedUser: State<String>
    val modal: State<Modal?>
    fun updateSearchType(index: Int)
    fun updateSearch(search: String)
    fun onProfileClicked()
    fun onUserClicked(user: User)
    fun onPostClicked(post: PostWithExtras)
    fun resetUserId()
}
