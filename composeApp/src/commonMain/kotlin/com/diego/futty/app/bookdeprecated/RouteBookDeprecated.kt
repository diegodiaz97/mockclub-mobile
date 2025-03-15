package com.diego.futty.app.bookdeprecated

import kotlinx.serialization.Serializable

sealed interface RouteBookDeprecated {

    @Serializable
    data object BookGraph: RouteBookDeprecated

    @Serializable
    data object BookList: RouteBookDeprecated

    @Serializable
    data class BookDetail(val id: String): RouteBookDeprecated
}