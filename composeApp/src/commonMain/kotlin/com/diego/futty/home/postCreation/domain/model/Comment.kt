package com.diego.futty.home.postCreation.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val userId: String,
    val text: String,
    val timestamp: Long, // used to display
    val serverTimestamp: Timestamp? = null, // used to order
)
