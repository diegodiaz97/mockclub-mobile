package com.diego.futty.home.design.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.feed.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface DesignViewContract {
    val searchText: StateFlow<String>
    val searchUsers: StateFlow<List<User>>
    val clickedUser: State<String>
    fun updateSearch(search: String)
    fun onProfileClicked()
    fun onUserClicked(user: User)
    fun resetUserId()
}
