package com.diego.futty.home.design.presentation.component.pro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorInfo
import compose.icons.Octicons
import compose.icons.octicons.Verified16

@Composable
fun VerifiedIcon(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .padding(1.dp)
                .clip(CircleShape)
                .background(colorInfo())
        )
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Octicons.Verified16,
            tint = colorGrey0(),
            contentDescription = null
        )
    }
}
