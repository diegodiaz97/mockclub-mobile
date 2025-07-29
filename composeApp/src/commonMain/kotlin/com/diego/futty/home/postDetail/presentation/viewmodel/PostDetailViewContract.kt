package com.diego.futty.home.postDetail.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.postCreation.domain.model.Comment
import com.diego.futty.home.postCreation.domain.model.CommentWithUser
import com.diego.futty.home.postCreation.domain.model.PostWithUser

interface PostDetailViewContract {
    val post: State<PostWithUser?>
    val comments: State<List<CommentWithUser>>
    val commentCreationProgress: State<Float>
    val commentToReply: State<CommentWithUser?>
    val likedCommentIds: State<Set<String>>
    val likedReplyIds: State<Set<String>>
    val repliesShown: State<List<String>>
    val repliesMap: State<Map<String, RepliesPaginationState>>
    fun getComments()
    fun onCommentClicked(comment: String)
    fun onLikeCommentClicked(comment: CommentWithUser, reply: CommentWithUser? = null)
    fun onReplyCommentClicked(comment: CommentWithUser)
    fun onShowRepliesClicked(comment: CommentWithUser)
    fun onHideRepliesClicked(comment: CommentWithUser)
    fun onLikeClicked()
    fun resetPost()
}
