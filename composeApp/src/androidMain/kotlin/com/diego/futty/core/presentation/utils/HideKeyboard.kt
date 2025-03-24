package com.diego.futty.core.presentation.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager

@Composable
actual fun HideKeyboard() {
    val context = LocalContext.current
    val inputMethodManager = context.getSystemService(InputMethodManager::class.java)
    val view = (context as? Activity)?.currentFocus
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

    val focusManager = LocalFocusManager.current
    focusManager.clearFocus()
}
