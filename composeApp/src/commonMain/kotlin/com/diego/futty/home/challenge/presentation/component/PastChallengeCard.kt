package com.diego.futty.home.challenge.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.User
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.image.AsyncImage

@Composable
fun PastChallengeCard(
    title: String,
    status: String,
    participants: String,
    background: String?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorGrey100())
            .clickable { }
    ) {
        val textColor = colorGrey900()

        if (background != null) {
            AsyncImage(
                modifier = Modifier
                    .clipToBounds()
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(
                    color = Grey900.copy(alpha = 0.2f),
                    blendMode = BlendMode.Darken
                ),
                contentDescription = "challenge",
                image = background
            )
        }

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorError())
                        .padding(horizontal = 12.dp),
                    text = status,
                    style = typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Grey0,
                    maxLines = 1,
                )

                Icon(
                    modifier = Modifier.padding(start = 4.dp).size(16.dp),
                    imageVector = PhosphorIcons.Fill.User,
                    tint = textColor,
                    contentDescription = null
                )

                Text(
                    text = participants,
                    style = typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    maxLines = 1,
                )
            }
        }
    }
}
