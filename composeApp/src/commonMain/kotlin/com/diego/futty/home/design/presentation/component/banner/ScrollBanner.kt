package com.diego.futty.home.design.presentation.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey900
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun ScrollBanner(
    modifier: Modifier = Modifier,
    bannerType: BannerType,
    items: List<BannerUIData>,
) {
    val itemsToShow = items.take(7)
    val state: PagerState = rememberPagerState { itemsToShow.size }
    PageControls(state)
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            pageSpacing = 12.dp,
            pageContent = { page: Int ->
                CreateBanner(bannerType, items[page], page, state)
            },
        )
        if (itemsToShow.size > 1) {
            DotIndicators(
                pageCount = itemsToShow.size,
                pagerState = state,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp),
            )
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
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) colorGrey900() else colorGrey200()
            Box(
                modifier =
                    Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color),
            )
        }
    }
}

@Composable
private fun CreateBanner(
    bannerType: BannerType,
    bannerUIData: BannerUIData,
    page: Int,
    state: PagerState,
) {
    when (bannerType) {
        BannerType.Display -> Banner.DisplayBanner(
            bannerUIData = bannerUIData,
            page = page,
            state = state,
        ).Draw()

        BannerType.FullImage -> Banner.FullImageBanner(
            bannerUIData = bannerUIData,
            page = page,
            state = state,
        ).Draw()
    }
}

@Composable
private fun PageControls(state: PagerState) {
    val autoScrollDuration = 3000L
    val isDragged by state.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(state) {
            var currentPageKey by remember { mutableIntStateOf(0) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }
}

fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        val transformation =
            lerp(
                start = 0.7f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        alpha = transformation
        scaleY = transformation
    }
