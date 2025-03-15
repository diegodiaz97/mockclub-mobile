package com.diego.futty.design.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.Grey0
import com.diego.futty.core.presentation.Grey200
import com.diego.futty.core.presentation.Grey600
import com.diego.futty.core.presentation.Info

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isEnabled) {
                    Info
                } else {
                    Grey200
                }
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        text = title,
        style = typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        color = if (isEnabled) {
            Grey0
        } else {
            Grey600
        },
        textAlign = TextAlign.Center
    )
}
