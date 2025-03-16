package com.diego.futty.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Graph: Route

    @Serializable
    data object Match: Route

    @Serializable
    data object Design: Route
}