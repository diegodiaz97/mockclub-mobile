package com.diego.futty.home.feed.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class ProfileImage(
    val image: DrawableResource? = null,
    val initials: String? = null,
    val background: String? = null,
)
