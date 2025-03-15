package com.diego.futty.core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun FuttyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = PoppinsTypography(),
        content = content
    )
}
