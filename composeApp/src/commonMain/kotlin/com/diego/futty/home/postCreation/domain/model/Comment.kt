package com.diego.futty.home.postCreation.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val userId: String,
    val text: String,
    val timestamp: Long, // used to display
    val likesCount: Int = 0,
    val likedByUser: Boolean = false,
    val replies: List<CommentWithUser> = emptyList(),
    val repliesCount: Int = 0,
    val serverTimestamp: Timestamp? = null, // used to order
)
