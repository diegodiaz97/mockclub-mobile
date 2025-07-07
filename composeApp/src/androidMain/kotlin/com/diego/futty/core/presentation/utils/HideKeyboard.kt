package com.diego.futty.core.presentation.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager

@Composable
actual fun HideKeyboard() {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Oculta el teclado
    val imm = context.getSystemService(InputMethodManager::class.java)
    val activity = context as? Activity
    val view = activity?.currentFocus ?: View(context) // fallback

    imm?.hideSoftInputFromWindow(view.windowToken, 0)

    // Limpia el foco también
    focusManager.clearFocus(force = true)
}
