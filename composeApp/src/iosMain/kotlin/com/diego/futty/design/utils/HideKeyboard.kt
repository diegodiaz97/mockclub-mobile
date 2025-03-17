package com.diego.futty.design.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UITextField
import platform.UIKit.UITextView
import platform.UIKit.endEditing

@Composable
actual fun HideKeyboard() {
    val window = UIApplication.sharedApplication().keyWindow
    val responder = window?.subviews?.find { it is UITextField }

    if (responder is UITextField) {
        responder.endEditing(true)
    }
    if (responder is UITextView) {
        responder.endEditing(true)
    }
}
