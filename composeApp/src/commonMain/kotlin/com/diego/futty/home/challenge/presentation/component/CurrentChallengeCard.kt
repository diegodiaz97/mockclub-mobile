package com.diego.futty.home.challenge.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.User
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.image.AsyncImage

@Composable
fun CurrentChallengeCard(
    time: String,
    title: String,
    subtitle: String,
    participants: String,
    background: String?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorGrey100())
            .clickable { }
    ) {
        val textColor = if (background != null) {
            Grey0
        } else {
            colorGrey900()
        }

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
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp, end = 32.dp).weight(1f),
                text = time,
                style = typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(textColor)
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                text = subtitle,
                style = typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                maxLines = 1,
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = PhosphorIcons.Fill.User,
                    tint = textColor,
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
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
