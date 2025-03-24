package com.diego.futty.home.design.presentation.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null,
    topBarActionType: TopBarActionType = TopBarActionType.None,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorGrey0())
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            TopBarIcon(TablerIcons.ArrowLeft, onBack)
            Spacer(Modifier.width(12.dp))
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = colorGrey900()
        )

        topBarActionType.Draw()
    }
}

@Composable
fun TopBarIcon(icon: ImageVector, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .clip(CircleShape)
            .background(colorGrey100())
            .clickable { onClick() }
            .size(36.dp)
            .padding(8.dp),
        imageVector = icon,
        tint = colorGrey900(),
        contentDescription = ""
    )
}
