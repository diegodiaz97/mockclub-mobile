package com.diego.futty.home.feed.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class ActionableImage(
    val image: DrawableResource,
    val onClick: (ActionableImage) -> Unit
)
