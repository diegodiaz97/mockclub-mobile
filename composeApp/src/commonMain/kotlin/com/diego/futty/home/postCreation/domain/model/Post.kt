package com.diego.futty.home.postCreation.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val createdAt: Long,
    val text: String,
    val team: String,
    val brand: String,
    val teamLogo: String,
    val brandLogo: String,
    val designerLogo: String? = null,
    val ratio: Float,
)
