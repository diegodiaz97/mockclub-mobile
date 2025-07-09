package com.diego.futty.home.design.presentation.component.image

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.diego.futty.core.presentation.photos.firebaseUrlToWorkerUrl
import com.diego.futty.core.presentation.theme.Shimmer
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.snapBackZoomable
import net.engawapg.lib.zoomable.zoomable

@Composable
fun AsyncImage(
    modifier: Modifier,
    shimmerModifier: Modifier? = null,
    colorFilter: ColorFilter? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "",
    image: String?,
) {
    val workerImageUrl = image?.let { firebaseUrlToWorkerUrl(it) }

    SubcomposeAsyncImage(
        modifier = modifier,
        model = workerImageUrl,
        contentScale = contentScale,
        colorFilter = colorFilter,
        contentDescription = contentDescription,
        loading = {
            Shimmer(modifier = shimmerModifier ?: modifier)
        }
    )
}

@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    image: String,
    contentScale: ContentScale = ContentScale.Crop,
    aspectRatio: Float = 1f,
    withSnapBackZoom: Boolean = false,
    onTap: (Offset) -> Unit = {},
    onLongPress: (Offset) -> Unit = {}
) {
    val zoomState = rememberZoomState(contentSize = Size.VisibilityThreshold)
    val settings = Settings()

    val selectedModifier = if (withSnapBackZoom) {
        modifier.snapBackZoomable(
            zoomState = zoomState,
            zoomEnabled = settings.zoomEnabled,
            onTap = onTap,
            onLongPress = onLongPress,
        )
    } else {
        modifier.zoomable(
            zoomState = zoomState,
            zoomEnabled = settings.zoomEnabled,
            enableOneFingerZoom = settings.enableOneFingerZoom,
            onTap = onTap,
            onLongPress = onLongPress,
        )
    }

    AsyncImage(
        modifier = selectedModifier.aspectRatio(aspectRatio),
        contentScale = contentScale,
        contentDescription = "zoomable image",
        image = image,
    )
}

data class Settings(
    val zoomEnabled: Boolean = true,
    val enableOneFingerZoom: Boolean = true,
    val scrollGesturePropagation: ScrollGesturePropagation = ScrollGesturePropagation.ContentEdge,
    val initialScale: Float = 1f,
)

enum class AspectRatio(val ratio: Float) {
    Square(1f),
    Portrait(4f / 5f)
}
