package com.diego.futty.home.postCreation.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val text: String,
    val imageUrls: List<String> = emptyList(),
    val ratio: Float,
    val timestamp: Long, // used to display
    val serverTimestamp: Timestamp, // used to order
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val likedByUser: Boolean = false,
    val team: String,
    val brand: String,
    val tags: List<String> = emptyList()
)
