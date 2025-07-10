package com.diego.futty.home.postDetail.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.postCreation.domain.model.CommentWithUser
import com.diego.futty.home.postCreation.domain.model.PostWithUser
import com.diego.futty.home.postCreation.domain.repository.PostRepository
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postRepository: PostRepository,
) : PostDetailViewContract, ViewModel() {

    private val _post = mutableStateOf<PostWithUser?>(null)
    override val post: State<PostWithUser?> = _post

    private val _comments = mutableStateOf<List<CommentWithUser>>(emptyList())
    override val comments: State<List<CommentWithUser>> = _comments

    private val _commentCreationProgress = mutableStateOf(0f)
    override val commentCreationProgress: State<Float> = _commentCreationProgress

    private var _loadingJob: Job? = null
    private var _lastTimestamp: Timestamp? = null
    private var _endReached = false

    fun setup(post: PostWithUser) {
        _post.value = post
        getComments()
    }

    private fun getComments() {
        val postId = _post.value?.post?.id ?: return

        viewModelScope.launch {
            postRepository.getComments(postId)
                .onSuccess { comments ->
                    _comments.value = comments
                }
                .onError {
                    //show error
                }
        }
    }

    override fun onCommentClicked(comment: String) {
        val postId = _post.value?.post?.id ?: return

        simulateLoading()

        viewModelScope.launch {
            postRepository.addComment(postId, comment)
                .onSuccess { comment ->
                    val currentComments = listOf(comment)
                    _comments.value = currentComments.plus(_comments.value)
                    _commentCreationProgress.value = 0f
                }
                .onError {
                    _commentCreationProgress.value = 0f
                }
        }
    }

    override fun onLikeClicked() {
        val alreadyLiked = post.value?.post?.likedByUser ?: false
        val current = _post.value
        _post.value = current?.copy(
            post = current.post.copy(
                likedByUser = !alreadyLiked,
                likesCount = if (alreadyLiked)
                    current.post.likesCount - 1
                else
                    current.post.likesCount + 1
            )
        )
    }

    private fun simulateLoading() {
        _loadingJob?.cancel()
        _loadingJob = viewModelScope.launch {
            _commentCreationProgress.value = 0f

            // Simular progreso parcial hasta 0.9
            while (_commentCreationProgress.value < 0.9f) {
                delay(20)
                _commentCreationProgress.value += 0.02f
            }
            finishProgressSmoothly()
            // Esperar que termine la operación real
            // (esto va en otro lugar como fetchFeed, y se llama finishProgressSmoothly())
        }
    }

    private suspend fun finishProgressSmoothly() {
        while (_commentCreationProgress.value < 1f) {
            delay(10)
            _commentCreationProgress.value += 0.03f
        }

        _commentCreationProgress.value = 1f

        if (_commentCreationProgress.value == 1f) {
            delay(3000)
            _commentCreationProgress.value = 0f
        }
    }
}
