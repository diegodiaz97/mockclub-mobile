package com.diego.futty.home.post.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val text: String,
    val imageUrls: List<String> = emptyList(),
    val timestamp: Long,
    val serverTimestamp: Timestamp,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val likedByUser: Boolean = false,
    val team: String,
    val brand: String,
    val tags: List<String> = emptyList()
)
