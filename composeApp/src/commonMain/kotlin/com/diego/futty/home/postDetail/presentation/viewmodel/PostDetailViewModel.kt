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
import dev.gitlive.firebase.firestore.toMilliseconds
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

    private val _commentToReply = mutableStateOf<CommentWithUser?>(null)
    override val commentToReply: State<CommentWithUser?> = _commentToReply

    private val _likedCommentIds = mutableStateOf<Set<String>>(emptySet())
    override val likedCommentIds: State<Set<String>> = _likedCommentIds

    private val _likedReplyIds = mutableStateOf<Set<String>>(emptySet())
    override val likedReplyIds: State<Set<String>> = _likedReplyIds

    private val _repliesShown = mutableStateOf<List<String>>(emptyList())
    override val repliesShown: State<List<String>> = _repliesShown

    private val _repliesMap = mutableStateOf<Map<String, RepliesPaginationState>>(emptyMap())
    override val repliesMap: State<Map<String, RepliesPaginationState>> = _repliesMap

    private var _loadingJob: Job? = null
    private var _lastTimestamp: Timestamp? = null
    private var _endReached = false

    fun setup(post: PostWithUser) {
        _post.value = post
        getCommentsLiked()
    }

    override fun resetPost() {
        _post.value = null
        _comments.value = emptyList()
        _repliesShown.value = emptyList()
        _likedCommentIds.value = emptySet()
        _likedReplyIds.value = emptySet()
        _repliesMap.value = emptyMap()
        _commentCreationProgress.value = 0f
        _commentToReply.value = null
        _loadingJob = null
        _lastTimestamp = null
        _endReached = false
    }

    override fun getComments() {
        val postId = _post.value?.post?.id ?: return

        viewModelScope.launch {
            postRepository.getComments(postId, 10, _lastTimestamp)
                .onSuccess { comments ->
                    val updatedComments = comments.map { commentWithUser ->
                        val liked = commentWithUser.comment.id in _likedCommentIds.value
                        commentWithUser.copy(
                            comment = commentWithUser.comment.copy(likedByUser = liked)
                        )
                    }

                    if (updatedComments.isEmpty()) {
                        if (_comments.value.isEmpty()) _comments.value = updatedComments
                        _endReached = true
                    } else {
                        _lastTimestamp = updatedComments.lastOrNull()?.comment?.serverTimestamp
                        _comments.value = _comments.value.plus(updatedComments)
                    }
                }
                .onError {
                    //show error
                }
        }
    }

    private fun getCommentsLiked() {
        val postId = _post.value?.post?.id ?: return
        viewModelScope.launch {
            val likes = postRepository.getLikedItemsForCurrentUser(postId)
            _likedCommentIds.value = likes.likedCommentIds
            _likedReplyIds.value = likes.likedReplyIds
            getComments()
        }
    }

    override fun onCommentClicked(comment: String) {
        if (_commentToReply.value != null) {
            onReplyClicked(comment)
        } else {
            onNewCommentClicked(comment)
        }
    }

    private fun onNewCommentClicked(comment: String) {
        val previousPost = _post.value ?: return
        simulateLoading()

        viewModelScope.launch {
            postRepository.addComment(previousPost.post.id, comment)
                .onSuccess { comment ->
                    val currentComments = listOf(comment)
                    _comments.value = currentComments.plus(_comments.value)
                    _commentCreationProgress.value = 0f

                    _post.value = previousPost.copy(
                        post = previousPost.post.copy(
                            commentsCount = previousPost.post.commentsCount + 1
                        )
                    )
                }
                .onError {
                    _commentCreationProgress.value = 0f
                }
        }
    }

    private fun onReplyClicked(reply: String) {
        val postId = _post.value?.post?.id ?: return
        val commentWithUser = _commentToReply.value ?: return

        simulateLoading()

        viewModelScope.launch {
            postRepository.replyToComment(postId, commentWithUser.comment.id, reply)
                .onSuccess { newReply ->
                    // Actualizar lista de comentarios con la nueva reply en el comentario correspondiente
                    _comments.value = _comments.value.map { cw ->
                        if (cw.comment.id == commentWithUser.comment.id) {
                            cw.copy(
                                comment = cw.comment.copy(
                                    repliesCount = cw.comment.repliesCount + 1
                                )
                            )
                        } else {
                            cw
                        }
                    }

                    val commentId = commentWithUser.comment.id
                    val currentState = _repliesMap.value[commentId]
                    val currentReplies = currentState?.replies ?: emptyList()

                    // Solo agregamos la nueva reply si ya cargamos todas las anteriores
                    val updatedReplies = if (currentState?.endReached == true) {
                        (currentReplies + newReply)
                            .distinctBy { it.comment.id }
                            .sortedBy { it.comment.serverTimestamp?.toMilliseconds() }
                    } else {
                        currentReplies // No agregamos nada, dejamos que venga por paginación
                    }

                    val lastTimestamp = updatedReplies.lastOrNull()?.comment?.serverTimestamp
                    val newEndReached = if (currentState?.endReached == true) {
                        updatedReplies.size >= commentWithUser.comment.repliesCount + 1
                    } else {
                        false
                    }

                    _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                        put(commentId, RepliesPaginationState(
                            replies = updatedReplies,
                            lastTimestamp = lastTimestamp,
                            endReached = newEndReached,
                            visible = true // Siempre mostramos las replies después de responder
                        ))
                    }

                    _commentCreationProgress.value = 0f
                }
                .onError {
                    _commentCreationProgress.value = 0f
                }
        }
    }

    override fun onReplyCommentClicked(comment: CommentWithUser) {
        _commentToReply.value = comment
    }

    override fun onShowRepliesClicked(comment: CommentWithUser) {
        val postId = _post.value?.post?.id ?: return
        val commentId = comment.comment.id
        val currentRepliesState = _repliesMap.value[commentId]
        val pageSize = 5

        // Caso 1: Ya hay replies cargadas y faltan más → paginar
        if (currentRepliesState != null && !currentRepliesState.endReached) {
            val lastTimestamp = currentRepliesState.lastTimestamp

            viewModelScope.launch {
                postRepository.getReplies(postId, commentId, limit = pageSize, startAfterTimestamp = lastTimestamp)
                    .onSuccess { newReplies ->
                        val updatedReplies = newReplies.map { reply ->
                            val liked = reply.comment.id in _likedReplyIds.value
                            reply.copy(comment = reply.comment.copy(likedByUser = liked))
                        }

                        val combinedReplies = (currentRepliesState.replies + updatedReplies)
                            .distinctBy { it.comment.id }
                            .sortedBy { it.comment.serverTimestamp?.toMilliseconds() }

                        _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                            put(commentId, RepliesPaginationState(
                                replies = combinedReplies,
                                lastTimestamp = updatedReplies.lastOrNull()?.comment?.serverTimestamp,
                                endReached = combinedReplies.size >= comment.comment.repliesCount,
                                visible = true
                            ))
                        }
                    }
                    .onError {
                        // manejo de error
                    }
            }
            return
        }

        // Caso 2: No hay replies cargadas aún → cargar primeras
        if (currentRepliesState == null) {
            viewModelScope.launch {
                postRepository.getReplies(postId, commentId, limit = pageSize, startAfterTimestamp = null)
                    .onSuccess { newReplies ->
                        val updatedReplies = newReplies.map { reply ->
                            val liked = reply.comment.id in _likedReplyIds.value
                            reply.copy(comment = reply.comment.copy(likedByUser = liked))
                        }

                        val sortedReplies = updatedReplies
                            .distinctBy { it.comment.id }
                            .sortedBy { it.comment.serverTimestamp?.toMilliseconds() }

                        _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                            put(commentId, RepliesPaginationState(
                                replies = sortedReplies,
                                lastTimestamp = sortedReplies.lastOrNull()?.comment?.serverTimestamp,
                                endReached = updatedReplies.size == comment.comment.repliesCount,
                                visible = true
                            ))
                        }
                    }
                    .onError {
                        // manejo de error
                    }
            }
            return
        }

        // Caso 3: replies ya cargadas y paginación terminada → toggle de visibilidad
        _repliesMap.value = _repliesMap.value.toMutableMap().apply {
            val current = this[commentId]
            if (current != null) {
                this[commentId] = current.copy(visible = !current.visible)
            }
        }
    }

    override fun onHideRepliesClicked(comment: CommentWithUser) {
        val commentId = comment.comment.id
        _repliesMap.value = _repliesMap.value.toMutableMap().apply {
            val current = get(commentId)
            if (current != null) {
                put(commentId, current.copy(visible = false))
            }
        }
    }

    override fun onLikeCommentClicked(comment: CommentWithUser, reply: CommentWithUser?) {
        val alreadyLiked = reply?.comment?.likedByUser ?: comment.comment.likedByUser
        val postId = _post.value?.post?.id ?: return
        val commentId = comment.comment.id
        val replyId = reply?.comment?.id

        // 1. Guardamos el estado actual para posible rollback
        val previousState = _comments.value

        // 2. Aplicamos la UI optimista
        _comments.value = _comments.value.map { cw ->
            if (cw.comment.id != commentId) return@map cw

            if (replyId == null) {
                val newLikesCount = if (alreadyLiked) comment.comment.likesCount - 1 else comment.comment.likesCount + 1

                cw.copy(
                    comment = cw.comment.copy(
                        likedByUser = !alreadyLiked,
                        likesCount = newLikesCount
                    )
                )
            } else {
                val updatedReplies = cw.comment.replies.map { rep ->
                    if (rep.comment.id == replyId) {
                        val newLikesCount = if (alreadyLiked) rep.comment.likesCount - 1 else rep.comment.likesCount + 1

                        rep.copy(
                            comment = rep.comment.copy(
                                likedByUser = !alreadyLiked,
                                likesCount = newLikesCount
                            )
                        )
                    } else rep
                }

                cw.copy(comment = cw.comment.copy(replies = updatedReplies))
            }
        }

        // 3. Llamada real al repo
        viewModelScope.launch {
            val result = if (alreadyLiked) {
                postRepository.removeLikeCommentOrReply(postId, commentId, replyId)
            } else {
                postRepository.likeCommentOrReply(postId, commentId, replyId)
            }

            // 4. Si falla, hacemos rollback
            result.onError {
                _comments.value = previousState
                // También podés mostrar un snackbar con "No se pudo dar like"
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

data class RepliesPaginationState(
    val replies: List<CommentWithUser> = emptyList(),
    val lastTimestamp: Timestamp? = null,
    val endReached: Boolean = false,
    val visible: Boolean = false
)
