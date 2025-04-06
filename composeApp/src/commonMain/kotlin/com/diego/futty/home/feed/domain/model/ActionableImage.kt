package com.diego.futty.home.feed.domain.model

data class ActionableImage(
    val image: String,
    val onClick: (ActionableImage) -> Unit
)
