package com.diego.futty.home.post.domain.model

import com.diego.futty.home.feed.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class PostWithUser(
    val post: Post,
    val user: User?
)
