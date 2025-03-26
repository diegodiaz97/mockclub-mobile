package com.diego.futty.home.feed.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.post.Draw
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressBar
import com.diego.futty.home.design.presentation.component.progressbar.LinearProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import compose.icons.TablerIcons
import compose.icons.tablericons.Menu2
import compose.icons.tablericons.X
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_cover
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel()
) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Futty",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.Menu2,
                    onClick = { viewModel.onProfileClicked() }
                )
            )
        },
        content = { paddingValues ->
            FeedContent(viewModel, paddingValues)
        },
    )
    OpenedImage(viewModel)
}

@Composable
private fun FeedContent(viewModel: FeedViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.width(4.dp))
            CircularProgressBar(progress = 0.2f, text = "Nivel 1")
            CircularProgressBar(progress = 0.4f, text = "Nivel 2", color = colorErrorLight())
            CircularProgressBar(progress = 0.6f, text = "Nivel 3", color = colorAlertLight())
            CircularProgressBar(progress = 0.8f, text = "Nivel 4", color = colorInfoLight())
            CircularProgressBar(progress = 1.0f, text = "Nivel 5", color = colorGrey700())
            Spacer(Modifier.width(4.dp))
        }
        LinearProgressBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            progress = 0.4f,
            color = colorGrey900()
        )
        PostsList(viewModel)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun OpenedImage(viewModel: FeedViewModel) {
    val image = viewModel.openedImage.value?.image
    if (image != null) {
        FlexibleBottomSheet(
            containerColor = colorGrey0().copy(alpha = 0.9f),
            scrimColor = Color.Transparent,
            onDismissRequest = { viewModel.onImageClosed() },
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
                Image(
                    modifier = Modifier.fillMaxSize().align(Alignment.Center),
                    painter = painterResource(image),
                    contentDescription = stringResource(Res.string.book_cover),
                )
                Avatar.IconAvatar(
                    modifier = Modifier
                        .padding(top = if (PlatformInfo.isAndroid) 12.dp else 60.dp, end = 16.dp)
                        .align(Alignment.TopEnd),
                    icon = TablerIcons.X,
                    background = colorGrey100(),
                    onClick = { viewModel.onImageClosed() }
                ).Draw()
            }
        }
    }
}

@Composable
private fun PostsList(viewModel: FeedViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.posts.value?.forEach { post ->
            item { post.Draw() }
        }
    }
}
