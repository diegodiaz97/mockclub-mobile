package com.diego.futty.app

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable
    data object Graph: AppRoute

    @Serializable
    data object Match: AppRoute

    @Serializable
    data object Design: AppRoute
}