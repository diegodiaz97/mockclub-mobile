package com.diego.futty.home.postCreation.domain.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val text: String,
    val brand: String,
    val team: String,
    val ratio: Float,
    val images: List<String>,
    val tags: List<String>
)
