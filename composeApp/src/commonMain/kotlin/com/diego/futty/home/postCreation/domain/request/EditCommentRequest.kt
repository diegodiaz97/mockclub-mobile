package com.diego.futty.home.postCreation.domain.request

import kotlinx.serialization.Serializable

@Serializable
data class EditCommentRequest(
    val text: String
)
