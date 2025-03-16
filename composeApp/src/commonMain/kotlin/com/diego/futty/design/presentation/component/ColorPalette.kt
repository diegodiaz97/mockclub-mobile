package com.diego.futty.design.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey900

@Composable
fun ColorPalette(title: String, background: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = background)
            .padding(vertical = 12.dp, horizontal = 16.dp),
    ) {
        Text(
            text = title,
            style = typography.titleLarge,
            color = colorGrey900()
        )
    }
}
