package com.diego.futty.app.presentation.view

import kotlinx.serialization.Serializable

sealed interface HomeRoute {

    @Serializable
    data object HomeGraph : HomeRoute

    @Serializable
    data object Match : HomeRoute

    @Serializable
    data object Design : HomeRoute

    @Serializable
    data object Setup : HomeRoute
}
