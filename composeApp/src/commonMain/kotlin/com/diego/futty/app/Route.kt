package com.diego.futty.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object BookGraph: Route

    @Serializable
    data object BookList: Route

    @Serializable
    data class BookDetail(val id: String): Route



    @Serializable
    data object Match: Route

    @Serializable
    data object Design: Route
}