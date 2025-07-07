package com.diego.futty.home.design.presentation.component.button

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = colorGrey900(),
    isEnabled: Boolean = true,
    border: Boolean = false,
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (border) colorGrey200() else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                if (isEnabled) {
                    onClick()
                }
            }
            .padding(vertical = 6.dp),
        text = title,
        style = typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        color = if (isEnabled) {
            color
        } else {
            colorGrey600()
        },
        textAlign = TextAlign.Center
    )
}
