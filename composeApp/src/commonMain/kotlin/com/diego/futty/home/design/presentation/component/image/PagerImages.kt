package com.diego.futty.home.design.presentation.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import compose.icons.TablerIcons
import compose.icons.tablericons.X

@Composable
fun PagerImages(
    modifier: Modifier = Modifier.fillMaxWidth(),
    images: List<String>,
    index: Int,
    aspectRatio: Float = 1f,
    isFullscreen: Boolean = false,
    onImageClosed: (() -> Unit)? = null,
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
                    }
                },
            )
            if (images.size > 1 && isFullscreen) {
                DotIndicators(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp),
                    pageCount = images.size,
                    pagerState = state,
                )
            }
            if (onImageClosed != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Avatar.IconAvatar(
                        icon = TablerIcons.X,
                        background = colorGrey100(),
                        onClick = { onImageClosed() }
                    ).Draw()
                }
            }
        }

        if (images.size > 1 && isFullscreen.not()) {
            DotIndicators(
                modifier = Modifier,
                pageCount = images.size,
                pagerState = state,
            )
        }
    }
}

@Composable
private fun DotIndicators(
    modifier: Modifier,
    pageCount: Int,
    pagerState: PagerState,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                colorGrey900()
            } else {
                colorGrey500().copy(alpha = 0.5f)
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
