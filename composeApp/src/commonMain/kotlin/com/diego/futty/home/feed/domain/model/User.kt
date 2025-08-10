package com.diego.futty.home.feed.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val username: String,
    val creationDate: Long,
    val userType: String,
    val name: String? = null,
    val lastName: String? = null,
    val description: String? = null,
    val profileImage: ProfileImage? = null,
    val country: String? = null,
)
