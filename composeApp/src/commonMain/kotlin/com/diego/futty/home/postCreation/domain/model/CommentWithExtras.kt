package com.diego.futty.home.postCreation.domain.model

import com.diego.futty.home.feed.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class CommentWithExtras(
    val comment: Comment,
    val user: User,
    val likeCount: Int,
    val likedByCurrentUser: Boolean,
    val replies: List<CommentWithExtras> = emptyList(),
    val replyCount: Int,
)
