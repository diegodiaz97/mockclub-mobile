package com.diego.futty.home.design.presentation.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
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
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight

sealed interface Banner {
    @Composable
    fun Draw()

    class DisplayBanner(
        val bannerUIData: BannerUIData,
        val page: Int = 0,
        val state: PagerState,
    ) : Banner {
        @Composable
        override fun Draw() = Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { bannerUIData.action?.invoke() }
                .background(colorGrey100())
                .carouselTransition(page, state)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (bannerUIData.illustration != null) {
                Avatar.FullImageAvatar(
                    imageUrl = bannerUIData.illustration,
                    avatarSize = bannerUIData.size,
                ).Draw()
            } else if (bannerUIData.icon != null) {
                Avatar.IconAvatar(
                    icon = bannerUIData.icon,
                    avatarSize = bannerUIData.size,
                ).Draw()
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = bannerUIData.title,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900(),
                )
                Text(
                    text = bannerUIData.description,
                    style = typography.bodySmall,
                    color = colorGrey800(),
                )
            }
            if (bannerUIData.action != null) {
                Icon(
                    modifier = Modifier.width(24.dp),
                    imageVector = TablerIcons.ChevronRight,
                    tint = colorGrey900(),
                    contentDescription = null
                )
            }
        }
    }

    class FullImageBanner(
        val bannerUIData: BannerUIData,
        val page: Int,
        val state: PagerState,
    ) : Banner {
        @Composable
        override fun Draw() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bannerUIData.color ?: colorGrey0())
                    .carouselTransition(page, state)
                    .clickable { bannerUIData.action?.invoke() }
            ) {
                val textColor =
                    if (bannerUIData.illustration != null) {
                        Grey0
                    } else {
                        colorGrey900()
                    }

                if (bannerUIData.illustration != null) {
                    AsyncImage(
                        modifier = Modifier
                            .clipToBounds()
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxSize(),
                        colorFilter = ColorFilter.tint(
                            color = Grey900.copy(alpha = 0.4f),
                            blendMode = BlendMode.Darken
                        ),
                        contentDescription = "profile image",
                        image = bannerUIData.illustration
                    )
                }

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = bannerUIData.title,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp, end = 32.dp).weight(1f),
                        text = bannerUIData.description,
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        color = textColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (bannerUIData.label != null) {
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(textColor)
                                .padding(vertical = 4.dp, horizontal = 12.dp)
                            ,
                            text = bannerUIData.label,
                            style = typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = Grey900,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }

    class StatusBanner(
        val bannerUIData: BannerUIData,
    ) : Banner {
        @Composable
        override fun Draw() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = if (bannerUIData.status == BannerStatus.Border) colorGrey200() else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        when (bannerUIData.status) {
                            BannerStatus.Success -> colorSuccessLight()
                            BannerStatus.Error -> colorErrorLight()
                            BannerStatus.Alert -> colorAlertLight()
                            BannerStatus.Info -> colorInfoLight()
                            BannerStatus.Border -> colorGrey0()
                            else -> colorGrey0()
                        }
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = bannerUIData.title,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (bannerUIData.status == BannerStatus.Border) colorGrey900() else Grey900,
                )
                Text(
                    text = bannerUIData.description,
                    style = typography.bodySmall,
                    color = if (bannerUIData.status == BannerStatus.Border) colorGrey900() else Grey900,
                )
            }
        }
    }
}

data class BannerUIData(
    val title: String,
    val description: String,
    val label: String? = null,
    val illustration: String? = null,
    val icon: ImageVector? = null,
    val size: AvatarSize = AvatarSize.Big,
    val color: Color? = null,
    val status: BannerStatus? = null,
    val action: (() -> Unit)? = null
)

enum class BannerType {
    Display,
    FullImage,
}

enum class BannerStatus {
    Success,
    Error,
    Alert,
    Info,
    Border,
}
