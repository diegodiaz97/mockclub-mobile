package com.diego.futty.core.presentation.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.endEditing

@Composable
actual fun HideKeyboard() {
    val windows = UIApplication.sharedApplication.windows
    val count = windows.count()

    for (i in 0 until count) {
        val window = windows[i] as? UIWindow
        if (window?.hidden == false && window.rootViewController != null) {
            window.endEditing(true)
            break
        }
    }
}
