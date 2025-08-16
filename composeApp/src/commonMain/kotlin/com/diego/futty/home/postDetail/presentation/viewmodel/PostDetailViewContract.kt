package com.diego.futty.home.postDetail.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.postCreation.domain.model.CommentWithExtras
import com.diego.futty.home.postCreation.domain.model.PostWithExtras

interface PostDetailViewContract {
    val post: State<PostWithExtras?>
    val comments: State<List<CommentWithExtras>>
    val commentCreationProgress: State<Float>
    val commentToReply: State<CommentWithExtras?>
    val repliesMap: State<Map<String, RepliesPaginationState>>
    fun getComments()
    fun onCommentClicked(text: String)
    fun onLikeCommentClicked(comment: CommentWithExtras, reply: CommentWithExtras? = null)
    fun onReplyCommentClicked(comment: CommentWithExtras)
    fun onShowRepliesClicked(comment: CommentWithExtras)
    fun onHideRepliesClicked(comment: CommentWithExtras)
    fun onLikeClicked()
    fun resetPost()
}
