package com.diego.futty.home.design.presentation.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey900

@Composable
fun SimpleInfoCard(
    modifier: Modifier = Modifier.fillMaxWidth(),
    title: String,
    description: String,
    border: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                if (border) colorGrey200() else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .background(colorGrey0())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900(),
        )
        Text(
            text = description,
            style = typography.bodySmall,
            fontWeight = FontWeight.Normal,
            color = colorGrey900(),
        )
    }
}
