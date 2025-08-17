package com.diego.futty.home.postDetail.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.postCreation.domain.model.CommentWithExtras
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.postCreation.domain.repository.PostRepository
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postRepository: PostRepository,
) : PostDetailViewContract, ViewModel() {

    private val _post = mutableStateOf<PostWithExtras?>(null)
    override val post: State<PostWithExtras?> = _post

    private val _comments = mutableStateOf<List<CommentWithExtras>?>(null)
    override val comments: State<List<CommentWithExtras>?> = _comments

    private val _commentCreationProgress = mutableStateOf(0f)
    override val commentCreationProgress: State<Float> = _commentCreationProgress

    private val _commentToReply = mutableStateOf<CommentWithExtras?>(null)
    override val commentToReply: State<CommentWithExtras?> = _commentToReply

    private val _repliesMap = mutableStateOf<Map<String, RepliesPaginationState>>(emptyMap())
    override val repliesMap: State<Map<String, RepliesPaginationState>> = _repliesMap

    // Paginación de comentarios principales
    private var _commentsEndReached = false
    private var _lastCommentTimestamp: Long? = null

    fun setup(post: PostWithExtras) {
        _post.value = post
        getComments()
    }

    override fun resetPost() {
        _post.value = null
        _comments.value = null
        _repliesMap.value = emptyMap()
        _commentCreationProgress.value = 0f
        _commentToReply.value = null
        _commentsEndReached = false
        _lastCommentTimestamp = null
    }

    override fun getComments() {
        val postId = _post.value?.post?.id ?: return
        if (_commentsEndReached) return

        viewModelScope.launch {
            postRepository.getComments(postId, limit = 10, cursorCreatedAt = _lastCommentTimestamp)
                .onSuccess { newComments ->
                    val comments = _comments.value ?: emptyList()

                    if (newComments.isEmpty()) {
                        _commentsEndReached = true
                        return@onSuccess
                    }

                    _lastCommentTimestamp = newComments.last().comment.createdAt
                    _comments.value = comments + newComments
                }
                .onError {
                    // manejar error
                }
        }
    }

    override fun onReplyCommentClicked(comment: CommentWithExtras) {
        _commentToReply.value = comment
    }

    override fun onCommentClicked(text: String) {
        val postId = _post.value?.post?.id ?: return
        val replyingTo = _commentToReply.value

        if (replyingTo != null) {
            addReply(postId, replyingTo.comment.id, text)
        } else {
            addComment(postId, text)
        }
    }

    private fun addComment(postId: String, text: String) {
        viewModelScope.launch {
            _commentCreationProgress.value = 0.3f
            postRepository.addComment(postId, text)
                .onSuccess { newComment ->
                    val comments = _comments.value ?: emptyList()
                    _comments.value = listOf(newComment) + comments
                    _post.value = _post.value?.copy(commentCount = _post.value!!.commentCount + 1)
                    _commentCreationProgress.value = 1f
                    _commentToReply.value = null
                }
                .onError {
                    _commentCreationProgress.value = 0f
                }
        }
    }
    // Agregar una reply
    private fun addReply(postId: String, parentCommentId: String, text: String) {
        viewModelScope.launch {
            _commentCreationProgress.value = 0.3f

            postRepository.addComment(postId, text, parentCommentId)
                .onSuccess { newReply ->
                    val currentState = _repliesMap.value[parentCommentId]

                    // Replies locales se agregan a la lista, pero no afectan lastTimestamp
                    val updatedReplies = if (currentState != null) {
                        (currentState.replies + newReply)
                            .distinctBy { it.comment.id }
                            .sortedBy { it.comment.createdAt }
                    } else {
                        listOf(newReply)
                    }

                    _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                        put(
                            parentCommentId, RepliesPaginationState(
                                replies = updatedReplies,
                                // lastTimestamp sigue siendo el de backend
                                lastTimestamp = currentState?.lastTimestamp,
                                endReached = currentState?.endReached ?: false,
                                visible = true
                            )
                        )
                    }

                    // Actualizar contador de comentarios del post
                    _post.value = _post.value?.copy(
                        commentCount = (_post.value?.commentCount ?: 0) + 1
                    )

                    _commentCreationProgress.value = 0f
                    _commentToReply.value = null
                }
                .onError {
                    _commentCreationProgress.value = 0f
                }
        }
    }

    override fun onShowRepliesClicked(comment: CommentWithExtras) {
        val commentId = comment.comment.id
        val currentState = _repliesMap.value[commentId]
        val pageSize = 5

        // 1) Primera carga
        if (currentState == null) {
            viewModelScope.launch {
                postRepository.getReplies(
                    commentId = commentId,
                    limit = pageSize,
                    cursorCreatedAt = null // primera página
                ).onSuccess { page ->
                    // page viene en DESC (más nuevas primero)
                    _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                        put(
                            commentId,
                            RepliesPaginationState(
                                replies = page,                                    // NO ordenar
                                lastTimestamp = page.lastOrNull()?.comment?.createdAt, // la más vieja de la página
                                endReached = page.size < pageSize,                  // si vino menos que el page size, no hay más
                                visible = true
                            )
                        )
                    }
                }
            }
            return
        }

        // 2) Si ya cargaste tod, sólo toggle de visibilidad
        if (currentState.endReached) {
            _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                put(commentId, currentState.copy(visible = !currentState.visible))
            }
            return
        }

        // 3) Pedir más (más viejas que lastTimestamp). NO recalcules nada raro: usá el cursor guardado.
        viewModelScope.launch {
            postRepository.getReplies(
                commentId = commentId,
                limit = pageSize,
                cursorCreatedAt = currentState.lastTimestamp // backend: createdAt < cursor
            ).onSuccess { page ->
                // También viene DESC. Sólo append, sin ordenar.
                val combined = (currentState.replies + page)
                    .distinctBy { it.comment.id } // por si acaso

                _repliesMap.value = _repliesMap.value.toMutableMap().apply {
                    put(
                        commentId,
                        currentState.copy(
                            replies = combined,
                            lastTimestamp = page.lastOrNull()?.comment?.createdAt
                                ?: currentState.lastTimestamp,
                            endReached = page.size < pageSize,
                            visible = true
                        )
                    )
                }
            }
        }
    }

    override fun onHideRepliesClicked(comment: CommentWithExtras) {
        val commentId = comment.comment.id
        _repliesMap.value = _repliesMap.value.toMutableMap().apply {
            get(commentId)?.let { put(commentId, it.copy(visible = false)) }
        }
    }

    override fun onLikeCommentClicked(comment: CommentWithExtras, reply: CommentWithExtras?) {
        val alreadyLiked = reply?.likedByCurrentUser ?: comment.likedByCurrentUser
        val postId = _post.value?.post?.id ?: return
        val commentId = comment.comment.id
        val replyId = reply?.comment?.id

        // UI optimista
        _comments.value = _comments.value?.map { cw ->
            if (cw.comment.id != commentId) return@map cw

            if (replyId == null) {
                cw.copy(
                    likedByCurrentUser = !alreadyLiked,
                    likeCount = cw.likeCount + if (alreadyLiked) -1 else 1
                )
            } else {
                val updatedReplies = cw.replies.map { r ->
                    if (r.comment.id == replyId) r.copy(
                        likedByCurrentUser = !alreadyLiked,
                        likeCount = r.likeCount + if (alreadyLiked) -1 else 1
                    ) else r
                }
                cw.copy(replies = updatedReplies)
            }
        }

        viewModelScope.launch {
            val result = if (alreadyLiked) {
                postRepository.removeLikeCommentOrReply(postId, commentId, replyId)
            } else {
                postRepository.likeCommentOrReply(postId, commentId, replyId)
            }
            result.onError {
                // rollback opcional
            }
        }
    }

    override fun onLikeClicked() {
        val alreadyLiked = post.value?.likedByCurrentUser ?: false
        val current = _post.value
        _post.value = current?.copy(
            likedByCurrentUser = !alreadyLiked,
            likeCount = if (alreadyLiked)
                current.likeCount - 1
            else
                current.likeCount + 1
        )
    }
}

data class RepliesPaginationState(
    val replies: List<CommentWithExtras> = emptyList(),
    val lastTimestamp: Long? = null,
    val endReached: Boolean = false,
    val visible: Boolean = false
)
