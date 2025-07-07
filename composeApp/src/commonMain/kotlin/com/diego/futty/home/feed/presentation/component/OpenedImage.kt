package com.diego.futty.home.feed.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.banner.carouselTransition
import com.diego.futty.home.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import compose.icons.TablerIcons
import compose.icons.tablericons.X

@Composable
fun OpenedImage(
    images: List<String>,
    index: Int,
    onImageClosed: () -> Unit,
) {
    if (images.isNotEmpty()) {
        BottomSheet(
            draggable = true,
            containerColor = colorGrey0().copy(alpha = 0.9f),
            onDismiss = { onImageClosed() },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                val state: PagerState = rememberPagerState(initialPage = index) { images.size }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    pageSpacing = 12.dp,
                    pageContent = { page: Int -> ImageContent(images[page], page, state) },
                )
                if (images.size > 1) {
                    DotIndicators(
                        pageCount = images.size,
                        pagerState = state,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp),
                    )
                }
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
    }
}

@Composable
private fun DotIndicators(
    pageCount: Int,
    pagerState: PagerState,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) colorGrey900() else colorGrey500().copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

@Composable
private fun ImageContent(
    image: String?,
    page: Int,
    state: PagerState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .carouselTransition(page, state)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            contentDescription = "opened image",
            image = image
        )
    }
}
