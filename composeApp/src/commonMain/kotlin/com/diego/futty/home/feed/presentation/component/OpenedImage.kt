package com.diego.futty.home.feed.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import compose.icons.TablerIcons
import compose.icons.tablericons.X

@Composable
fun OpenedImage(
    image: String?,
    onImageClosed: () -> Unit,
) {
    if (image != null) {
        FlexibleBottomSheet(
            containerColor = colorGrey0().copy(alpha = 0.9f),
            scrimColor = Color.Transparent,
            onDismissRequest = { onImageClosed() },
            sheetState = rememberFlexibleBottomSheetState(
                flexibleSheetSize = FlexibleSheetSize(
                    fullyExpanded = if (PlatformInfo.isAndroid) 1f else 0.91f
                ),
                isModal = true,
                skipIntermediatelyExpanded = true,
                skipSlightlyExpanded = true,
            ),
            windowInsets = WindowInsets(0.dp),
            dragHandle = {},
        ) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize().align(Alignment.Center),
                    contentScale = ContentScale.Fit,
                    contentDescription = "opened image",
                    image = image
                )

                Avatar.IconAvatar(
                    modifier = Modifier
                        .padding(top = if (PlatformInfo.isAndroid) 12.dp else 60.dp, end = 16.dp)
                        .align(Alignment.TopEnd),
                    icon = TablerIcons.X,
                    background = colorGrey100(),
                    onClick = { onImageClosed() }
                ).Draw()
            }
        }
    }
}
