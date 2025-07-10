package com.diego.futty.home.postDetail.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.postCreation.domain.model.CommentWithUser
import com.diego.futty.home.postCreation.domain.model.PostWithUser

interface PostDetailViewContract {
    val post: State<PostWithUser?>
    val comments: State<List<CommentWithUser>>
    val commentCreationProgress: State<Float>
    fun onCommentClicked(comment: String)
    fun onLikeClicked()
}
