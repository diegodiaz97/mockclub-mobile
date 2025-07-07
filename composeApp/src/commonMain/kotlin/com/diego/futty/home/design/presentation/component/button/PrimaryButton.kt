package com.diego.futty.home.design.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    color: Color = colorGrey900(),
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isEnabled) {
                    color
                } else {
                    colorGrey200()
                }
            )
            .clickable {
                if (isEnabled) {
                    onClick()
                }
            }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = icon,
                tint = if (isEnabled) {
                    colorGrey0()
                } else {
                    colorGrey600()
                },
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            style = typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (isEnabled) {
                colorGrey0()
            } else {
                colorGrey600()
            },
            textAlign = TextAlign.Start
        )
    }
}
