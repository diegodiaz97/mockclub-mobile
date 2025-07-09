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
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.Job
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

    private val _openedImage = mutableStateOf<List<String>>(emptyList())
    override val openedImage: State<List<String>> = _openedImage

    private val _openedImageIndex = mutableStateOf(0)
    override val openedImageIndex: State<Int> = _openedImageIndex

    private val _openedImageRatio = mutableStateOf(1f)
    override val openedImageRatio: State<Float> = _openedImageRatio

    private val _isRefreshing = mutableStateOf(false)
    override val isRefreshing: State<Boolean> = _isRefreshing

    private val _postCreationProgress = mutableStateOf(0f)
    override val postCreationProgress: State<Float> = _postCreationProgress

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private val _likedPostIds = mutableStateOf<Set<String>>(emptySet())
    private var _loadingJob: Job? = null
    private var _lastTimestamp: Timestamp? = null
    private var _endReached = false
    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        fetchUserInfo()
        fetchPostsLiked()
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

    override fun onImageClicked(
        images: List<String>,
        index: Int,
        ratio: Float
    ) {
        _openedImage.value = images
        _openedImageIndex.value = index
        _openedImageRatio.value = ratio
    }

    override fun onImageClosed() {
        _openedImage.value = emptyList()
        _openedImageIndex.value = 0
        _openedImageIndex.value = 0
        _openedImageRatio.value = 1f
    }

    override fun onPostClicked(post: PostWithUser) {
        _openedPost.value = post
        _navigate(HomeRoute.PostDetail)
    }

    override fun onLikeClicked(post: PostWithUser, fromDetail: Boolean) {
        val postId = post.post.id
        val alreadyLiked = post.post.likedByUser

        if (fromDetail) {
            val current = _openedPost.value
            _openedPost.value = current?.copy(
                post = current.post.copy(
                    likedByUser = !alreadyLiked,
                    likesCount = if (alreadyLiked)
                        current.post.likesCount - 1
                    else
                        current.post.likesCount + 1
                )
            )
        }

        // ✅ 1. Optimistic UI update
        _likedPostIds.value = if (alreadyLiked) {
            _likedPostIds.value - postId
        } else {
            _likedPostIds.value + postId
        }

        _posts.value = _posts.value?.map { current ->
            if (current.post.id == postId) {
                current.copy(
                    post = current.post.copy(
                        likedByUser = !alreadyLiked,
                        likesCount = if (alreadyLiked)
                            current.post.likesCount - 1
                        else
                            current.post.likesCount + 1
                    )
                )
            } else current
        }

        viewModelScope.launch {
            val result = if (alreadyLiked) {
                postRepository.removeLike(postId)
            } else {
                postRepository.likePost(postId)
            }

            // ❌ 3. Si falla, revertir cambios
            result.onError {
                // 🔁 revertir el set
                _likedPostIds.value = if (alreadyLiked) {
                    _likedPostIds.value + postId
                } else {
                    _likedPostIds.value - postId
                }

                // 🔁 revertir la UI
                _posts.value = _posts.value?.map { current ->
                    if (current.post.id == postId) {
                        current.copy(
                            post = current.post.copy(
                                likedByUser = alreadyLiked,
                                likesCount = if (alreadyLiked)
                                    current.post.likesCount + 1
                                else
                                    current.post.likesCount - 1
                            )
                        )
                    } else current
                }

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
                    val updatedPosts = newPosts.map { postWithUser ->
                        val liked = postWithUser.post.id in _likedPostIds.value
                        postWithUser.copy(
                            post = postWithUser.post.copy(likedByUser = liked)
                        )
                    }

                    if (updatedPosts.isEmpty()) {
                        if (_posts.value == null) _posts.value = updatedPosts
                        _endReached = true
                    } else {
                        _lastTimestamp = updatedPosts.lastOrNull()?.post?.serverTimestamp
                        _posts.value = _posts.value?.plus(updatedPosts) ?: updatedPosts
                    }
                }
                .onError {
                    _loadingJob?.cancel()
                    _postCreationProgress.value = 0f

                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun onStartPostCreation() {
        simulateLoading()
    }

    override fun onPostCreated() {
        onFeedRefreshed()
    }

    override fun showPostCreation() {
        _navigate(HomeRoute.CreatePost)
    }

    override fun onFeedRefreshed() {
        _endReached = false
        _lastTimestamp = null
        _posts.value = null
        _openedPost.value = null
        _openedImage.value = emptyList()
        _openedImageIndex.value = 0
        _openedImageRatio.value = 1f
        fetchFeed()
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

    private fun fetchPostsLiked() {
        viewModelScope.launch {
            val userId = preferences.getUserId() ?: return@launch
            _likedPostIds.value = postRepository.getLikedPostIdsForUser(userId)
        }
    }

    private fun simulateLoading() {
        _loadingJob?.cancel()
        _loadingJob = viewModelScope.launch {
            _postCreationProgress.value = 0f

            // Simular progreso parcial hasta 0.9
            while (_postCreationProgress.value < 0.9f) {
                delay(20)
                _postCreationProgress.value += 0.02f
            }
            finishProgressSmoothly()
            // Esperar que termine la operación real
            // (esto va en otro lugar como fetchFeed, y se llama finishProgressSmoothly())
        }
    }

    private suspend fun finishProgressSmoothly() {
        while (_postCreationProgress.value < 1f) {
            delay(10)
            _postCreationProgress.value += 0.03f
        }

        _postCreationProgress.value = 1f

        if (_postCreationProgress.value == 1f) {
            delay(3000)
            _postCreationProgress.value = 0f
        }
    }
}
