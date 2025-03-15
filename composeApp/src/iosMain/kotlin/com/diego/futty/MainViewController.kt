package com.diego.futty

import androidx.compose.ui.window.ComposeUIViewController
import com.diego.futty.app.App
import com.diego.futty.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }