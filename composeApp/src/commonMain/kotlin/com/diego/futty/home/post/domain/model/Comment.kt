package com.diego.futty.home.post.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val userId: String,
    val text: String,
    val timestamp: Long,
)
