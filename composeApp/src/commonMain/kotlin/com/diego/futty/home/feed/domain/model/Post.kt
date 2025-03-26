package com.diego.futty.home.feed.domain.model

data class Post(
    val id: String,
    val user: User,
    val date: String,
    val likes: List<User>? = null,
    // val comments: List<Comment>? = null,
    val text: String? = null,
    val images: List<ActionableImage>? = null,
    val desires: List<String>? = null,
    val onClick: (Post) -> Unit
)
