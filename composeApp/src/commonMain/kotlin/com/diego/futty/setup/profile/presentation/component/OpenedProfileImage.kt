package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import compose.icons.TablerIcons
import compose.icons.tablericons.X

@Composable
fun OpenedProfileImage(
    userId: Boolean,
    image: String?,
    initials: String?,
    background: Color?,
    showUpdateImage: () -> Unit,
    onLaunchCamera: () -> Unit,
    onLaunchGallery: () -> Unit,
) {
    FlexibleBottomSheet(
        containerColor = colorGrey0().copy(alpha = 0.9f),
        scrimColor = Color.Transparent,
        onDismissRequest = { showUpdateImage() },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 1f
            ),
            isModal = true,
            skipIntermediatelyExpanded = true,
            skipSlightlyExpanded = true,
        ),
        windowInsets = WindowInsets(0.dp),
        dragHandle = {},
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    image?.isNotEmpty() == true -> {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape),
                            shimmerModifier = Modifier
                                .padding(horizontal = 24.dp)
                                .clip(CircleShape),
                            contentDescription = "profile image",
                            image = image
                        )
                    }

                    initials != null -> {
                        val color = background ?: colorGrey900()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 50.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                fontWeight = FontWeight.Bold,
                                style = typography.headlineLarge,
                                color = colorGrey900(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                if (userId) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SecondaryButton(
                            title = "Elegir de la galería",
                            color = colorGrey900(),
                            onClick = {
                                showUpdateImage()
                                onLaunchGallery()
                            }
                        )
                        SecondaryButton(
                            title = "Tomar foto",
                            color = colorGrey900(),
                            onClick = {
                                showUpdateImage()
                                onLaunchCamera()
                            }
                        )
                    }
                }
            }
            Avatar.IconAvatar(
                modifier = Modifier
                    .padding(top = if (PlatformInfo.isAndroid) 12.dp else 60.dp, end = 16.dp)
                    .align(Alignment.TopEnd),
                icon = TablerIcons.X,
                background = colorGrey100(),
                onClick = { showUpdateImage() }
            ).Draw()
        }
    }
}
