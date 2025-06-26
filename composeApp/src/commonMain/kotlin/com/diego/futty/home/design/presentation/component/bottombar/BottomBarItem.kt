package com.diego.futty.home.design.presentation.component.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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

@Composable
fun BottomBarItem(
    icon: ImageVector,
    text: String,
    color: Color,
    tint: Color,
    onClick: () -> Unit,
) = Column(
    modifier = Modifier
        .padding(horizontal = 4.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick.invoke() }
        .background(color)
        .padding(horizontal = 12.dp, vertical = 6.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = icon,
        tint = tint,
        contentDescription = null
    )
    Text(
        text = text,
        style = typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = tint,
    )
}
