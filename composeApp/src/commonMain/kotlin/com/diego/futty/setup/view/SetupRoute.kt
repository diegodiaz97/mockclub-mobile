package com.diego.futty.setup.view

import kotlinx.serialization.Serializable

sealed interface SetupRoute {
    @Serializable
    data object SetupGraph : SetupRoute

    @Serializable
    data object Profile : SetupRoute

    @Serializable
    data object ProfileCreation : SetupRoute

    @Serializable
    data object Setting : SetupRoute

    @Serializable
    data object Authentication : SetupRoute

    @Serializable
    data object PostDetail : SetupRoute
}
