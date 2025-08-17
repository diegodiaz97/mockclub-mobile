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
import com.diego.futty.home.design.presentation.component.bottomsheet.OptionItem
import com.diego.futty.home.design.presentation.component.bottomsheet.OptionType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.feed.presentation.component.Keys
import com.diego.futty.home.feed.presentation.component.tryShowReveal
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.postCreation.domain.repository.PostRepository
import com.diego.futty.home.view.HomeRoute
import com.svenjacobs.reveal.RevealState
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

    private val _posts = mutableStateOf<List<PostWithExtras>?>(null)
    override val posts: State<List<PostWithExtras>?> = _posts

    private val _openedPost = mutableStateOf<PostWithExtras?>(null)
    override val openedPost: State<PostWithExtras?> = _openedPost

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

    private val _clickedUser = mutableStateOf("")
    override val clickedUser: State<String> = _clickedUser

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _loadingJob: Job? = null
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

    override fun onPostClicked(post: PostWithExtras) {
        _openedPost.value = post
        _navigate(HomeRoute.PostDetail)
    }

    override fun onLikeClicked(post: PostWithExtras, fromDetail: Boolean) {
        val postId = post.post.id
        val alreadyLiked = post.likedByCurrentUser
        val newLiked = !alreadyLiked
        val delta = if (newLiked) 1 else -1

        // Guardar snapshots para rollback
        val prevPosts = _posts.value
        val prevOpened = _openedPost.value

        // Actualización optimista en lista
        _posts.value = _posts.value?.map { current ->
            if (current.post.id == postId) {
                current.copy(
                    likedByCurrentUser = newLiked,
                    likeCount = (current.likeCount + delta).coerceAtLeast(0)
                )
            } else current
        }

        // Actualización optimista en detalle si corresponde
        if (fromDetail) {
            _openedPost.value = _openedPost.value?.copy(
                likedByCurrentUser = newLiked,
                likeCount = ((_openedPost.value?.likeCount ?: (0 + delta))).coerceAtLeast(0)
            )
        }

        // Llamada a backend
        viewModelScope.launch {
            val result = if (newLiked) {
                postRepository.likePost(postId)
            } else {
                postRepository.unlikePost(postId)
            }

            result.onError {
                // Rollback si falla
                _posts.value = prevPosts
                _openedPost.value = prevOpened

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
                    // Fusionar con estado actual para no perder likes optimistas
                    val mergedPosts = newPosts.map { fresh ->
                        val localVersion = _posts.value?.firstOrNull { it.post.id == fresh.post.id }
                        if (localVersion != null) {
                            // Mantener likeCount y likedByCurrentUser locales
                            fresh.copy(
                                likedByCurrentUser = localVersion.likedByCurrentUser,
                                likeCount = localVersion.likeCount
                            )
                        } else {
                            fresh // Nuevo post, se usa tal cual viene del backend
                        }
                    }

                    if (mergedPosts.size < 15) {
                        _endReached = true
                    }

                    if (mergedPosts.isEmpty()) {
                        if (_posts.value == null) _posts.value = mergedPosts
                    } else {
                        _lastTimestamp = mergedPosts.lastOrNull()?.post?.createdAt
                        _posts.value = _posts.value?.plus(mergedPosts) ?: mergedPosts
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

    override fun updateLikes(userMadeLike: Boolean) {
        if (userMadeLike) {
            onFeedRefreshed()
        }
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

    override fun onChallengesClicked() {
        _navigate(HomeRoute.Challenge)
    }

    override fun onUserClicked(user: User) {
        val currentUser = preferences.getUserId() ?: return
        if (user.id != currentUser) {
            _clickedUser.value = user.id
        }
        _navigate(HomeRoute.Setup)
    }

    override fun resetUserId() {
        _clickedUser.value = ""
    }

    override fun onOptionsClicked(post: PostWithExtras) {
        val currentUser = preferences.getUserId() ?: return

        val option = OptionItem(
            text = "Opción prueba",
            action = { _modal.value = null },
        )

        val negativeOption = if (currentUser == post.user.id) {
            OptionItem(
                text = "Eliminar post",
                action = { deletePost(post.post.id) },
                type = OptionType.Error,
            )
        } else {
            OptionItem(
                text = "Denunciar post",
                action = { reportPost(post.post.id, "") },
                type = OptionType.Error,
            )
        }

        _modal.value = Modal.OptionsModal(
            options = listOf(option, negativeOption),
            onDismiss = { _modal.value = null },
        )
    }

    private fun deletePost(postId: String) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
                .onSuccess {
                    onFeedRefreshed()
                }
                .onError {
                    // SHOW ERROR
                }
        }
    }

    private fun reportPost(postId: String, reason: String) {
        viewModelScope.launch {
            postRepository.reportPost(postId, reason)
                .onSuccess {
                    onFeedRefreshed()
                }
                .onError {
                    // SHOW ERROR
                }
        }
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
