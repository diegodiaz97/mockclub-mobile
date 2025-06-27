package com.diego.futty.home.design.presentation.component.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.diego.futty.core.presentation.photos.firebaseUrlToWorkerUrl
import com.diego.futty.core.presentation.theme.Shimmer

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
