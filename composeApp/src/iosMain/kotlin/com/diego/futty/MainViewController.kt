package com.diego.futty

import androidx.compose.ui.window.ComposeUIViewController
import com.diego.futty.authentication.view.AuthenticationView
import com.diego.futty.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { AuthenticationView() }