package com.diego.futty.home.postCreation.domain.model

import com.diego.futty.home.feed.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class PostWithExtras(
    val post: Post,
    val user: User,
    val images: List<String>,
    val tags: List<Tag>,
    val likeCount: Int,
    val commentCount: Int,
    val likedByCurrentUser: Boolean
)
