package com.diego.futty.home.design.presentation.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.X
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey300
import com.diego.futty.core.presentation.theme.Grey800
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.avatar.Avatar

@Composable
fun PagerImages(
    modifier: Modifier = Modifier.fillMaxWidth(),
    images: List<String>,
    index: Int,
    aspectRatio: Float = 1f,
    isFullscreen: Boolean = false,
    onImageClosed: (() -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val state: PagerState = rememberPagerState(initialPage = index) { images.size }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = modifier,
        ) {
            HorizontalPager(
                modifier = modifier,
                state = state,
                pageSpacing = 0.dp,
                pageContent = { page: Int ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ZoomableImage(
                            modifier = Modifier.fillMaxWidth(),
                            image = images[page],
                            aspectRatio = aspectRatio,
                            withSnapBackZoom = isFullscreen.not(),
                            onTap = { },
                            onLongPress = { }
                        )
                        if (images.size > 1) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(CircleShape)
                                    .background(Grey800.copy(alpha = 0.4f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                text = "${page + 1}/${images.size}",
                                style = typography.labelSmall,
                                fontWeight = FontWeight.Normal,
                                color = Grey0,
                            )
                        }
                    }
                },
            )

            footer?.invoke()

            if (images.size > 1) {
                DotIndicators(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (isFullscreen) 32.dp else 16.dp),
                    pageCount = images.size,
                    pagerState = state,
                    invertColors = isFullscreen.not()
                )
            }
            if (onImageClosed != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Avatar.IconAvatar(
                        icon = PhosphorIcons.Bold.X,
                        background = colorGrey100(),
                        onClick = { onImageClosed() }
                    ).Draw()
                }
            }
        }
    }
}

@Composable
private fun DotIndicators(
    modifier: Modifier,
    pageCount: Int,
    pagerState: PagerState,
    invertColors: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val selected = if (invertColors) Grey0 else colorGrey900()
        val unselected = if (invertColors) Grey300 else colorGrey500()

        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                selected
            } else {
                unselected.copy(alpha = 0.5f)
            }
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
