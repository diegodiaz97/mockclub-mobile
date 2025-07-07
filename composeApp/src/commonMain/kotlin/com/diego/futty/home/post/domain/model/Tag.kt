package com.diego.futty.home.post.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val usageCount: Int = 1
)
