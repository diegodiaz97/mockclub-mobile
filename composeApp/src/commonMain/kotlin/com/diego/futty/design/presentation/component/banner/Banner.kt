package com.diego.futty.design.presentation.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey800
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.core.presentation.theme.colorSuccessLight
import com.diego.futty.design.presentation.component.avatar.Avatar
import com.diego.futty.design.presentation.component.avatar.AvatarSize
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight

sealed interface Banner {
    @Composable
    fun Draw()

    class ClickableBanner(
        val image: Painter? = null,
        val title: String,
        val subtitle: String,
        val onClick: () -> Unit
    ) : Banner {
        @Composable
        override fun Draw() = Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick.invoke() }
                .background(colorGrey100())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (image != null) {
                Avatar.ImageAvatar(
                    image = image,
                    avatarSize = AvatarSize.Big,
                ).Draw()
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900(),
                )
                Text(
                    text = subtitle,
                    style = typography.bodySmall,
                    color = colorGrey800(),
                )
            }
            Icon(
                modifier = Modifier.width(24.dp),
                imageVector = TablerIcons.ChevronRight,
                tint = colorGrey900(),
                contentDescription = null
            )
        }
    }

    class ErrorBanner(
        val title: String,
        val subtitle: String,
    ) : Banner {
        @Composable
        override fun Draw() {
            DrawInformation(
                title,
                subtitle,
                colorErrorLight()
            )
        }
    }

    class SuccessBanner(
        val title: String,
        val subtitle: String,
    ) : Banner {
        @Composable
        override fun Draw() {
            DrawInformation(
                title,
                subtitle,
                colorSuccessLight()
            )
        }
    }

    class AlertBanner(
        val title: String,
        val subtitle: String,
    ) : Banner {
        @Composable
        override fun Draw() {
            DrawInformation(
                title,
                subtitle,
                colorAlertLight()
            )
        }
    }

    class InfoBanner(
        val title: String,
        val subtitle: String,
    ) : Banner {
        @Composable
        override fun Draw() {
            DrawInformation(
                title,
                subtitle,
                colorInfoLight()
            )
        }
    }

    class BorderBanner(
        val title: String,
        val subtitle: String,
    ) : Banner {
        @Composable
        override fun Draw() {
            DrawInformation(
                title,
                subtitle,
                colorGrey0(),
                true,
            )
        }
    }
}

@Composable
private fun DrawInformation(
    title: String,
    subtitle: String,
    color: Color,
    border: Boolean = false,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .border(
            width = 1.dp,
            color = if (border) colorGrey200() else Color.Transparent,
            shape = RoundedCornerShape(12.dp)
        )
        .background(color)
        .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
) {
    Text(
        text = title,
        style = typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = if (border) colorGrey900() else Grey900,
    )
    Text(
        text = subtitle,
        style = typography.bodySmall,
        color = if (border) colorGrey900() else Grey900,
    )
}
