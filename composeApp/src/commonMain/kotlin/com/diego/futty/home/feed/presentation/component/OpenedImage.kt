package com.diego.futty.home.feed.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.home.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.home.design.presentation.component.image.PagerImages

@Composable
fun OpenedImage(
    images: List<String>,
    index: Int,
    aspectRatio: Float = 1f,
    onImageClosed: () -> Unit,
) {
    if (images.isNotEmpty()) {
        BottomSheet(
            draggable = true,
            containerColor = colorGrey0().copy(alpha = 0.9f),
            onDismiss = { onImageClosed() },
        ) {
            PagerImages(
                modifier = Modifier.fillMaxSize(),
                images = images,
                index = index,
                aspectRatio = aspectRatio,
                isFullscreen = true,
                onImageClosed = onImageClosed,
            )
        }
    }
}
