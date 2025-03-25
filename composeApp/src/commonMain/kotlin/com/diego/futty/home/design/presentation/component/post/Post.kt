package com.diego.futty.home.design.presentation.component.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_cover
import org.jetbrains.compose.resources.stringResource

@Composable
fun Post(
    profileImage: Avatar,
    name: String,
    date: String,
    text: String? = null,
    image: Painter? = null,
) = Box(Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth().height(1.dp),
            color = colorGrey200(),
        )
        PostInformation(profileImage, name, date)
        if (text != null) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = colorGrey900(),
            )
        }
        if (image != null) {
            Image(
                painter = image,
                contentDescription = stringResource(Res.string.book_cover),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorGrey100())
            )
        }
    }
}

@Composable
private fun PostInformation(
    profileImage: Avatar,
    title: String,
    subtitle: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        profileImage.Draw()
        Column(verticalArrangement = Arrangement.SpaceAround) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey900()
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = subtitle,
                style = typography.bodySmall,
                fontWeight = FontWeight.Normal,
                color = colorGrey600()
            )
        }
    }
}
