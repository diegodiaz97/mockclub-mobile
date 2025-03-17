package com.diego.futty.authentication.view

import kotlinx.serialization.Serializable

sealed interface AuthenticationRoute {
    @Serializable
    data object Graph: AuthenticationRoute

    @Serializable
    data object Welcome: AuthenticationRoute

    @Serializable
    data object Signup: AuthenticationRoute

    @Serializable
    data object Login: AuthenticationRoute

    @Serializable
    data object Recovery: AuthenticationRoute

    @Serializable
    data object Home: AuthenticationRoute
}
