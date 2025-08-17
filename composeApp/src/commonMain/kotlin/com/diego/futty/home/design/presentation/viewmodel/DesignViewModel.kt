package com.diego.futty.home.design.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.data.repository.DiscoverRepositoryImpl
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.design.presentation.util.SearchType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.view.HomeRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DesignViewModel(
    private val discoverRepository: DiscoverRepositoryImpl,
    private val preferences: UserPreferences,
) : DesignViewContract, ViewModel() {

    private val _searchTypes = mutableStateOf(listOf("Usuarios", "Equipos", "Marcas", "Tags"))
    override val searchTypes: State<List<String>> = _searchTypes

    private val _selectedSearchType = mutableStateOf(0)
    override val selectedSearchType: State<Int> = _selectedSearchType

    private val _searchText = MutableStateFlow("")
    override val searchText: StateFlow<String> = _searchText

    private val _searchUsers = MutableStateFlow<List<User>?>(null)
    override val searchUsers: StateFlow<List<User>?> = _searchUsers

    private val _searchPosts = MutableStateFlow<List<PostWithExtras>?>(null)
    override val searchPosts: StateFlow<List<PostWithExtras>?> = _searchPosts

    private val _openedPost = mutableStateOf<PostWithExtras?>(null)
    override val openedPost: State<PostWithExtras?> = _openedPost

    private val _clickedUser = mutableStateOf("")
    override val clickedUser: State<String> = _clickedUser

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        debounce()
        _navigate = { navController.navigate(it) }
    }

    override fun updateSearchType(index: Int) {
        resetSearch()
        _selectedSearchType.value = index
        debounce()
    }

    override fun onProfileClicked() {
        _navigate(HomeRoute.Setup)
    }

    @OptIn(FlowPreview::class)
    private fun debounce() {
        viewModelScope.launch {
            _searchText.debounce(500) // Espera 300ms después de la última pulsación
                .filter { it.isNotEmpty() } // No busca si el texto está vacío
                .collectLatest { query ->
                    resetSearch()

                    if (_selectedSearchType.value == 0) {
                        searchUsers(query)
                    } else {
                        searchPosts(query)
                    }
                }
        }
    }

    private suspend fun searchUsers(query: String) = withContext(Dispatchers.IO) {
        discoverRepository.searchUsers(query)
            .onSuccess { users ->
                _searchUsers.value = users
            }
            .onError {
                _modal.value = Modal.GenericErrorModal(
                    onPrimaryAction = { _modal.value = null },
                    onDismiss = { _modal.value = null },
                )
            }
    }

    private suspend fun searchPosts(query: String) = withContext(Dispatchers.IO) {
        val userId = preferences.getUserId() ?: ""

        discoverRepository.searchPosts(
            currentUserId = userId,
            query = query,
            type = when (_selectedSearchType.value) {
                1 -> SearchType.TEAM
                2 -> SearchType.BRAND
                else -> SearchType.TAG
            },
            limit = 10,
            offset = 0
        ).onSuccess { posts ->
            _searchPosts.value = posts
        }.onError {
            _modal.value = Modal.GenericErrorModal(
                onPrimaryAction = { _modal.value = null },
                onDismiss = { _modal.value = null },
            )
        }
    }

    override fun updateSearch(search: String) {
        resetSearch()
        _searchText.value = search
        if (search.isEmpty()) {
            _searchUsers.value = emptyList()
        }
    }

    override fun onUserClicked(user: User) {
        val currentUser = preferences.getUserId() ?: return
        if (user.id != currentUser) {
            _clickedUser.value = user.id
        }
        _navigate(HomeRoute.Setup)
    }

    override fun onPostClicked(post: PostWithExtras) {
        _openedPost.value = post
        _navigate(HomeRoute.PostDetail)
    }

    override fun resetUserId() {
        _clickedUser.value = ""
    }

    private fun resetSearch() {
        _searchPosts.value = null
        _searchUsers.value = null
    }
}
