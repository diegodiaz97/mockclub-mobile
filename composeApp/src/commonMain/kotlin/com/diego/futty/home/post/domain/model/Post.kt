package com.diego.futty.home.post.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val text: String,
    val imageUrls: List<String> = emptyList(),
    val timestamp: Long,
    val likesCount: Int = 0,
    val dislikesCount: Int = 0,
    val commentsCount: Int = 0,
    val team: String,
    val brand: String,
)
