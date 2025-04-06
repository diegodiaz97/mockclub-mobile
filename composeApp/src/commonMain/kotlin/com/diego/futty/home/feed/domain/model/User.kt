package com.diego.futty.home.feed.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val lastName: String? = null,
    val description: String? = null,
    val profileImage: ProfileImage,
    val creationDate: String,
    val followers: List<String>? = null, // Followers ID's
    val following: List<String>? = null, // Following ID's
    val level: Int,
    val country: String? = null,
    val desires: List<String>? = null,
)
