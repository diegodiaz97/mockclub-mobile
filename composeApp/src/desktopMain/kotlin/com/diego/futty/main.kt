package com.diego.futty

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.diego.futty.authentication.view.AuthenticationView
import com.diego.futty.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Futty",
        ) {
            AuthenticationView()
        }
    }
}