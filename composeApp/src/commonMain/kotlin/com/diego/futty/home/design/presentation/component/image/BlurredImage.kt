package com.diego.futty.home.design.presentation.component.image

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_cover
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlurredImage(
    image: Painter,
    blur: Dp = 0.dp,
    content: (@Composable (Modifier) -> Unit)? = null,
) = Box(Modifier) {
    Box(Modifier.fillMaxWidth()) {
        Image(
            painter = image,
            contentDescription = stringResource(Res.string.book_cover),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .blur(blur)
        )
    }
    if (content != null) {
        content(Modifier.fillMaxSize().align(Alignment.BottomCenter).padding(12.dp))
    }
}
