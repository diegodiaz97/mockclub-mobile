package com.diego.futty.home.feed.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val description: String? = null,
    val profileImage: ProfileImage,
    val creationDate: String,
    val followers: List<User>?,
    val following: List<User>?,
    val level: Int,
    val country: String,
    val desires: List<String>? = null,
)
