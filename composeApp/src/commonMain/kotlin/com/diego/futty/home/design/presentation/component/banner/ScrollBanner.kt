package com.diego.futty.home.design.presentation.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.SubcomposeAsyncImage
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.Shimmer
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey900
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowRight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun ScrollBanner(
    modifier: Modifier = Modifier,
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
                CreateBanner(items[page], page, state)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) colorGrey900() else colorGrey200()
            Box(
                modifier =
                    Modifier
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
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bannerUIData.color ?: colorGrey100())
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
            SubcomposeAsyncImage(
                modifier = Modifier.clipToBounds().fillMaxSize(),
                model = bannerUIData.illustration,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(
                    color = Grey900.copy(alpha = 0.5f),
                    blendMode = BlendMode.Darken
                ),
                contentDescription = "profile image",
                loading = {
                    Shimmer(modifier = Modifier.clipToBounds().fillMaxSize())
                }
            )
        }

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = bannerUIData.title,
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp, end = 32.dp).weight(1f),
                text = bannerUIData.description,
                style = typography.titleSmall,
                fontWeight = FontWeight.Normal,
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = bannerUIData.labelAction,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    maxLines = 1,
                )
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = TablerIcons.ArrowRight,
                    tint = textColor,
                    contentDescription = null
                )
            }
        }
    }
}

data class BannerUIData(
    val title: String,
    val description: String,
    val labelAction: String,
    val illustration: String? = null,
    val color: Color? = null,
    val action: (() -> Unit)? = null
)

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
