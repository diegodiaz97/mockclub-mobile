package com.diego.futty.design.presentation.component.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey900

sealed interface Avatar {
    @Composable
    fun Draw()

    class FullImageAvatar(
        val image: Painter,
        val avatarSize: AvatarSize = AvatarSize.Medium,
        val onClick: (() -> Unit)? = null,
    ) : Avatar {
        @Composable
        override fun Draw() = Image(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick?.invoke() }
                .background(colorGrey0())
                .size(avatarSize.size),
            contentScale = ContentScale.Crop,
            painter = image,
            contentDescription = null
        )
    }

    class ImageAvatar(
        val image: Painter,
        val avatarSize: AvatarSize = AvatarSize.Medium,
        val onClick: (() -> Unit)? = null,
    ) : Avatar {
        @Composable
        override fun Draw() = Image(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick?.invoke() }
                .background(colorGrey0())
                .size(avatarSize.size)
                .padding(avatarSize.padding),
            painter = image,
            contentDescription = null
        )
    }

    class IconAvatar(
        val icon: ImageVector,
        val tint: Color? = null,
        val background: Color? = null,
        val avatarSize: AvatarSize = AvatarSize.Medium,
        val onClick: (() -> Unit)? = null,
    ) : Avatar {
        @Composable
        override fun Draw() = Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick?.invoke() }
                .background(background ?: colorGrey0())
                .size(avatarSize.size)
                .padding(avatarSize.padding),
            imageVector = icon,
            tint = tint ?: colorGrey900(),
            contentDescription = null
        )
    }

    class InitialsAvatar(
        val initials: String,
        val tint: Color? = null,
        val background: Color? = null,
        val avatarSize: AvatarSize = AvatarSize.Medium,
        val onClick: (() -> Unit)? = null,
    ) : Avatar {
        @Composable
        override fun Draw() = Text(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick?.invoke() }
                .background(background ?: colorGrey0())
                .size(avatarSize.size)
                .padding(top = avatarSize.padding),
            text = initials,
            style = when (avatarSize) {
                AvatarSize.Small -> typography.titleSmall
                AvatarSize.Medium -> typography.bodyMedium
                AvatarSize.Big -> typography.headlineSmall
            },
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = tint ?: colorGrey900(),
        )
    }
}

enum class AvatarSize(val size: Dp, val padding: Dp) {
    Small(size = 28.dp, padding = 4.dp),
    Medium(size = 36.dp, padding = 8.dp),
    Big(size = 50.dp, padding = 10.dp)
}
