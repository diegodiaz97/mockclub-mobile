package com.diego.futty.home.feed.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImage(
    val image: String? = null,
    val initials: String? = null,
    val background: String,
)
