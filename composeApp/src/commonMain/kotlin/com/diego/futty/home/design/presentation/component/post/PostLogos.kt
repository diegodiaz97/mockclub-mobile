package com.diego.futty.home.design.presentation.component.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.PlusCircle
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.image.AsyncImage

@Composable
fun PostLogosWithGradient(
    modifier: Modifier,
    teamLogo: String,
    brandLogo: String,
    designerLogo: String?,
    isSmallImage: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isSmallImage) 70.dp else 90.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        // 🔹 Degradado negro abajo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Grey900.copy(alpha = 0.03f),
                            Grey900.copy(alpha = 0.06f),
                            Grey900.copy(alpha = 0.1f),
                            Grey900.copy(alpha = 0.2f),
                            Grey900.copy(alpha = 0.3f),
                            Grey900.copy(alpha = 0.4f),
                            Grey900.copy(alpha = 0.5f),
                            Grey900.copy(alpha = 0.6f),
                            Grey900.copy(alpha = 0.7f),
                            Grey900.copy(alpha = 0.8f),
                            Grey900.copy(alpha = 0.9f),
                            Grey900.copy(alpha = 0.95f),
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // 🔹 Logos nítidos
        val logosModifier = if (isSmallImage) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
        }
        Row(
            modifier = logosModifier.padding(if (isSmallImage) 12.dp else 16.dp),
            horizontalArrangement = if (isSmallImage) {
                if (designerLogo == null) Arrangement.SpaceEvenly else Arrangement.SpaceBetween
            } else {
                Arrangement.spacedBy(12.dp)
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val colorFilter = ColorFilter.tint(color = Grey0)
            LogoImage(Modifier, teamLogo, isSmallImage)
            VerticalDivider(
                modifier = Modifier.height(16.dp),
                thickness = 1.dp,
                color = Grey0.copy(alpha = 0.5f)
            )
            LogoImage(Modifier, brandLogo, isSmallImage, colorFilter)
            if (designerLogo != null) {
                VerticalDivider(
                    modifier = Modifier.height(16.dp),
                    thickness = 1.dp,
                    color = Grey0.copy(alpha = 0.5f)
                )
                LogoImage(Modifier, designerLogo, isSmallImage, colorFilter)
            }
            if (isSmallImage.not()) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun PostLogos(
    modifier: Modifier,
    teamLogo: String,
    brandLogo: String,
    designerLogo: String?,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val colorFilter = ColorFilter.tint(color = colorGrey900())
            LogoImage(Modifier, teamLogo)
            VerticalDivider(
                modifier = Modifier.height(16.dp),
                thickness = 1.dp,
                color = colorGrey900().copy(alpha = 0.5f)
            )
            LogoImage(Modifier, brandLogo, true, colorFilter)
            if (designerLogo != null) {
                VerticalDivider(
                    modifier = Modifier.height(16.dp),
                    thickness = 1.dp,
                    color = colorGrey900().copy(alpha = 0.5f)
                )
                LogoImage(Modifier, designerLogo, true, colorFilter)
            }
        }
    }
}

@Composable
private fun LogoImage(
    modifier: Modifier,
    logo: String,
    isSmallImage: Boolean = false,
    colorFilter: ColorFilter? = null,
) {
    val size = if (isSmallImage) 28.dp else 30.dp
    AsyncImage(
        modifier = modifier.size(size),
        shimmerModifier = modifier
            .alpha(0f)
            .size(size),
        colorFilter = colorFilter,
        contentScale = ContentScale.Fit,
        contentDescription = "logo image",
        image = logo
    )
}

// Selection

@Composable
fun PostLogosSelection(
    modifier: Modifier,
    teamLogo: ByteArray?,
    brandLogo: ByteArray?,
    designerLogo: ByteArray?,
    onLogoClicked: (String) -> Unit = {},
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val colorFilter = ColorFilter.tint(color = colorGrey900())
            LogoSelectionImage(Modifier, "team", teamLogo, null, onLogoClicked)

            VerticalDivider(
                modifier = Modifier.height(32.dp).padding(bottom = 16.dp),
                thickness = 1.dp,
                color = colorGrey400().copy(alpha = 0.5f)
            )
            LogoSelectionImage(Modifier, "brand", brandLogo, colorFilter, onLogoClicked)

            VerticalDivider(
                modifier = Modifier.height(32.dp).padding(bottom = 16.dp),
                thickness = 1.dp,
                color = colorGrey400().copy(alpha = 0.5f)
            )
            LogoSelectionImage(Modifier, "designer", designerLogo, colorFilter, onLogoClicked)
        }
    }
}

@Composable
private fun LogoSelectionImage(
    modifier: Modifier,
    type: String,
    logo: ByteArray?,
    colorFilter: ColorFilter?,
    onLogoClicked: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLogoClicked(type) },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (logo != null) {
            SubcomposeAsyncImage(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(36.dp)
                    .background(colorGrey100())
                    .padding(2.dp),
                model = logo,
                contentScale = ContentScale.Fit,
                colorFilter = colorFilter,
                contentDescription = "logo selected to post",
            )
        } else {
            Icon(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(36.dp)
                    .background(colorGrey100())
                    .padding(4.dp),
                imageVector = PhosphorIcons.Bold.PlusCircle,
                tint = colorGrey600(),
                contentDescription = "select logo to post"
            )
        }
        Text(
            modifier = modifier,
            text = when (type) {
                "team" -> "Equipo"
                "brand" -> "Marca"
                else -> "Autor"
            },
            style = typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = colorGrey600()
        )
    }
}
