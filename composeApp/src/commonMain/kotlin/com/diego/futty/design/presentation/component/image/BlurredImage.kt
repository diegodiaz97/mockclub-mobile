package com.diego.futty.design.presentation.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.design.presentation.component.avatar.Avatar
import com.diego.futty.design.presentation.component.avatar.AvatarSize
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_cover
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlurredImage(
    profileImage: Painter = painterResource(Res.drawable.girasoles),
    image: Painter,
    title: String = "Diego Díaz",
    subtitle: String = "Hace 10 horas",
    blur: Dp = 0.dp,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar.FullImageAvatar(
                image = profileImage,
                avatarSize = AvatarSize.Medium,
            ).Draw()
            Column {
                Text(
                    text = title,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                Text(
                    text = subtitle,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    color = colorGrey700()
                )
            }
        }
        Image(
            painter = image,
            contentDescription = stringResource(Res.string.book_cover),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .blur(blur)
        )
    }
}
