package com.diego.futty.core.presentation.utils

expect object PlatformInfo {
    val isAndroid: Boolean
    val isIOS: Boolean
    val isDesktop: Boolean
}
