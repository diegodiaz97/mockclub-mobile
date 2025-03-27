package com.diego.futty.home.design.presentation.component.Chip

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
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900

@Composable
fun Chip(
    icon: ImageVector? = null,
    text: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) = Row(
    modifier = Modifier
        .padding(horizontal = 4.dp)
        .clip(RoundedCornerShape(8.dp))
        .clickable { onClick.invoke() }
        .background(if (isSelected) color else colorGrey100())
        .padding(horizontal = 12.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(6.dp)
) {
    if (icon != null) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = icon,
            tint = if (isSelected) Grey900 else colorGrey900(),
            contentDescription = null
        )
    }
    Text(
        text = text,
        style = typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = if (isSelected) Grey900 else colorGrey900(),
    )
}
