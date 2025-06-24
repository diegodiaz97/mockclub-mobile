package com.diego.futty.home.design.presentation.component.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.SubcomposeAsyncImage
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.Shimmer
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun FullScreenPager(
    modifier: Modifier = Modifier,
    items: List<BannerUIData>,
) {
    val itemsToShow = items.take(7)
    val state: PagerState = rememberPagerState { itemsToShow.size }
    PageControls(state)
    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = state,
            pageSpacing = 12.dp,
            pageContent = { page: Int -> CreateBanner(items[page], page, state) },
        )
        if (itemsToShow.size > 1) {
            DotIndicators(
                pageCount = itemsToShow.size,
                pagerState = state,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) Grey0 else colorGrey500().copy(alpha = 0.5f)
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
private fun CreateBanner(
    bannerUIData: BannerUIData,
    page: Int,
    state: PagerState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bannerUIData.color ?: colorGrey0())
            .carouselTransition(page, state)
            .clip(RoundedCornerShape(12.dp))
            .clickable { bannerUIData.action?.invoke() }
    ) {
        val textColor =
            if (bannerUIData.illustration != null) {
                Grey0
            } else {
                colorGrey900()
            }

        if (bannerUIData.illustration != null) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxHeight(),
                model = bannerUIData.illustration,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    color = Grey900.copy(alpha = 0.7f),
                    blendMode = BlendMode.Darken
                ),
                contentDescription = "profile image",
                loading = {
                    Shimmer(modifier = Modifier.fillMaxSize())
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = bannerUIData.title,
                style = typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                maxLines = 2,
            )
            Text(
                modifier = Modifier.padding(top = 24.dp).weight(1f),
                text = bannerUIData.description,
                style = typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                text = bannerUIData.labelAction,
                style = typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
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

private fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
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
