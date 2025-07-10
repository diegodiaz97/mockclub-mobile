package com.diego.futty.home.postCreation.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val usageCount: Int = 1
)
