package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.feed.presentation.component.Keys
import com.diego.futty.home.feed.presentation.component.tryShowReveal
import com.diego.futty.home.post.domain.model.PostWithUser
import com.diego.futty.home.post.domain.repository.PostRepository
import com.diego.futty.home.view.HomeRoute
import com.svenjacobs.reveal.RevealState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedViewModel(
    private val profileCreationRepository: ProfileCreationRepository,
    private val postRepository: PostRepository,
    private val preferences: UserPreferences,
) : FeedViewContract, ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    override val user: State<User?> = _user

    private val _posts = mutableStateOf<List<PostWithUser>?>(null)
    override val posts: State<List<PostWithUser>?> = _posts

    private val _openedPost = mutableStateOf<PostWithUser?>(null)
    override val openedPost: State<PostWithUser?> = _openedPost

    private val _openedImage = mutableStateOf<String?>(null)
    override val openedImage: State<String?> = _openedImage

    private val _isRefreshing = mutableStateOf(false)
    override val isRefreshing: State<Boolean> = _isRefreshing

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private val _showPostCreation = mutableStateOf(false)
    override val showPostCreation: State<Boolean> = _showPostCreation

    private val _postMaxLength = mutableStateOf(100)
    override val postMaxLength: State<Int> = _postMaxLength

    private val _text = mutableStateOf("")
    override val text: State<String> = _text

    private val _team = mutableStateOf("")
    override val team: State<String> = _team

    private val _brand = mutableStateOf("")
    override val brand: State<String> = _brand

    private val _images = mutableStateOf<List<ByteArray>>(emptyList())
    override val images: State<List<ByteArray>> = _images

    private var _lastTimestamp: Long? = null
    private var _endReached = false
    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        fetchUserInfo()
        fetchFeed()
        _navigate = { navController.navigate(it) }
    }

    override fun startReveal(revealState: RevealState) {
        viewModelScope.launch {
            if (revealState.isVisible || preferences.getOnboarding() == true) return@launch
            delay(380)
            revealState.tryShowReveal(Keys.Profile)
            preferences.saveOnboarding(done = true)
        }
    }

    override fun onProfileClicked() {
        _navigate(HomeRoute.Setup)
    }

    override fun onImageClicked(image: String) {
        _openedImage.value = image
    }

    override fun onImageClosed() {
        _openedImage.value = null
    }

    override fun onPostClicked(post: PostWithUser) {
        _openedPost.value = post
    }

    override fun onPostClosed() {
        _openedPost.value = null
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = preferences.getUserId() ?: return@launch
            profileCreationRepository.fetchProfile(user)
                .onSuccess { loggedUser ->
                    _user.value = loggedUser
                    preferences.saveUserType(loggedUser.userType)
                }
                .onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun fetchFeed() {
        viewModelScope.launch {
            postRepository.getFeed(15, _lastTimestamp)
                .onSuccess { newPosts ->
                    if (newPosts.isEmpty()) {
                        if (_posts.value == null) {
                            _posts.value = newPosts
                        }
                        _endReached = true
                    } else {
                        _lastTimestamp = newPosts.lastOrNull()?.post?.timestamp
                        if (_posts.value == null) {
                            _posts.value = newPosts
                        } else {
                            _posts.value = _posts.value?.plus(newPosts)
                        }
                    }
                }
                .onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun createPost() {
        if (text.value.isEmpty() || team.value.isEmpty() || brand.value.isEmpty()) {
            _modal.value = Modal.GenericErrorModal(
                onPrimaryAction = { _modal.value = null },
                onDismiss = { _modal.value = null },
            )
            return
        }
        viewModelScope.launch {
            postRepository.createPost(
                text = _text.value,
                images = images.value,
                team = _team.value,
                brand = _brand.value,
            ).onSuccess {
                onFeedRefreshed()
            }.onError {
                _modal.value = Modal.GenericErrorModal(
                    onPrimaryAction = { _modal.value = null },
                    onDismiss = { _modal.value = null },
                )
            }
        }
    }

    override fun showPostCreation() {
        _showPostCreation.value = true
    }

    override fun dismissPostCreation() {
        _showPostCreation.value = false
    }

    override fun updateText(newText: String) {
        if (newText.length <= _postMaxLength.value) {
            _text.value = newText
        }
    }

    override fun updateTeam(newTeam: String) {
        _team.value = newTeam
    }

    override fun updateBrand(newBrand: String) {
        _brand.value = newBrand
    }

    override fun onFeedRefreshed() {
        _endReached = false
        _lastTimestamp = null
        _showPostCreation.value = false
        _posts.value = null
        fetchFeed()
    }
}
