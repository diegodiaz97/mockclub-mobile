package com.diego.futty.home.post.domain.model

import com.diego.futty.home.feed.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class CommentWithUser(
    val comment: Comment,
    val user: User?
)
