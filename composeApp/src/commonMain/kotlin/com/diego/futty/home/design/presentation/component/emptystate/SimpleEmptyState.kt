package com.diego.futty.home.design.presentation.component.emptystate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.diego.futty.core.presentation.theme.colorGrey900

@Composable
fun SimpleEmptyState(
    modifier: Modifier,
    text: String,
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = colorGrey900()
        )
    }
}
