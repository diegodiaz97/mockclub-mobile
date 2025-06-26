package com.diego.futty.home.design.presentation.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.diego.futty.core.presentation.theme.Shimmer
import com.diego.futty.core.presentation.theme.colorGrey100

@Composable
fun BlurredImage(
    image: String,
    blur: Dp = 0.dp,
    content: (@Composable (Modifier) -> Unit)? = null,
) = Box(Modifier) {
    Box(Modifier.fillMaxWidth()) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .blur(blur),
            model = image,
            contentScale = ContentScale.Crop,
            contentDescription = "blurred image",
            loading = {
                Shimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorGrey100())
                )
            }
        )
    }
    if (content != null) {
        content(Modifier.fillMaxSize().align(Alignment.BottomCenter).padding(12.dp))
    }
}
