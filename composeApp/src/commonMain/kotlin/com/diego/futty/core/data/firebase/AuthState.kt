package com.diego.futty.core.data.firebase

sealed class AuthState {
    object LoggedIn : AuthState()
    object LoggedOut : AuthState()
}
