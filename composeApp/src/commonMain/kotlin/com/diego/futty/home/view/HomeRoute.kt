package com.diego.futty.home.view

import kotlinx.serialization.Serializable

sealed interface HomeRoute {

    @Serializable
    data object HomeGraph : HomeRoute

    @Serializable
    data object Feed : HomeRoute

    @Serializable
    data object Match : HomeRoute

    @Serializable
    data object Design : HomeRoute

    @Serializable
    data object Setup : HomeRoute

    @Serializable
    data object CreatePost : HomeRoute

    @Serializable
    data object PostDetail : HomeRoute
}
