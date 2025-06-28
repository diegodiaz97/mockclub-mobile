package com.diego.futty.home.feed.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.banner.BannerType
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.banner.ScrollBanner
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.diego.futty.home.design.presentation.component.post.Draw
import com.diego.futty.home.design.presentation.component.post.PostShimmer
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.presentation.component.Keys
import com.diego.futty.home.feed.presentation.component.RevealOverlayContent
import com.diego.futty.home.feed.presentation.component.showNextReveal
import com.diego.futty.home.feed.presentation.component.tryShowReveal
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import com.svenjacobs.reveal.Reveal
import com.svenjacobs.reveal.RevealCanvasState
import com.svenjacobs.reveal.RevealShape
import com.svenjacobs.reveal.RevealState
import com.svenjacobs.reveal.revealable
import compose.icons.TablerIcons
import compose.icons.tablericons.Focus
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Shirt
import compose.icons.tablericons.X
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreen(
    revealCanvasState: RevealCanvasState,
    scope: CoroutineScope,
    revealState: RevealState,
    viewModel: FeedViewModel = koinViewModel()
) {
    LaunchedEffect(viewModel) { viewModel.startReveal(revealState) }

    Reveal(
        modifier = Modifier,
        revealCanvasState = revealCanvasState,
        revealState = revealState,
        overlayContent = { key -> RevealOverlayContent(key) },
        onOverlayClick = { key -> scope.launch { showNextReveal(revealState, key) } },
        onRevealableClick = { },
    ) {
        Scaffold(
            containerColor = colorGrey0(),
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp)
                        .revealable(
                            key = Keys.Profile,
                            state = revealState,
                            shape = RevealShape.RoundRect(16.dp),
                            onClick = { scope.launch { revealState.tryShowReveal(Keys.Explore) } }
                        ),
                    title = "MockClub",
                    topBarActionType = TopBarActionType.Profile(
                        imageUrl = viewModel.user.value?.profileImage?.image,
                        initials = viewModel.user.value?.profileImage?.initials,
                        background = viewModel.user.value?.profileImage?.background?.toColor(),
                        onClick = { viewModel.onProfileClicked() }
                    )
                )
            },
            content = { paddingValues ->
                FeedContent(viewModel, paddingValues, scope, revealState)
            },
        )
        OpenedImage(viewModel)
        viewModel.modal.value?.Draw()
    }
}

@Composable
private fun FeedContent(
    viewModel: FeedViewModel,
    paddingValues: PaddingValues,
    scope: CoroutineScope,
    revealState: RevealState,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PostsList(viewModel, scope, revealState)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Avatar.IconAvatar(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 20.dp)
                .revealable(
                    key = Keys.Post,
                    state = revealState,
                    shape = RevealShape.Circle,
                    onClick = { scope.launch { revealState.hide() } }
                ),
            avatarSize = AvatarSize.Big,
            icon = TablerIcons.Plus,
            background = colorError(),
            tint = Grey0,
            onClick = { }
        ).Draw()
    }
}

@Composable
private fun TopBanner(
    scope: CoroutineScope,
    revealState: RevealState
) {
    ScrollBanner(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .revealable(
                key = Keys.Explore,
                state = revealState,
                shape = RevealShape.RoundRect(16.dp),
                onClick = { scope.launch { revealState.tryShowReveal(Keys.Post) } }
            ),
        bannerType = BannerType.Display,
        items = listOf(
            BannerUIData(
                title = "Explorar",
                color = colorInfoLight(),
                description = "Usa la sección de Explorar para inspirarte.",
                icon = TablerIcons.Focus,
            ),
            BannerUIData(
                title = "Crear",
                color = colorInfoLight(),
                description = "¿Ya creaste tu primer mockup? Únete a miles de diseñadores.",
                icon = TablerIcons.Shirt,
            ),
        )
    )
}

@Composable
private fun OpenedImage(viewModel: FeedViewModel) {
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
                    onClick = { viewModel.onImageClosed() }
                ).Draw()
            }
        }
    }
}

@Composable
private fun PostsList(
    viewModel: FeedViewModel,
    scope: CoroutineScope,
    revealState: RevealState,
) {
    val posts = viewModel.posts.value
    if (posts != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { TopBanner(scope, revealState) }
            posts.forEachIndexed { index, post ->
                item {
                    post.Draw()
                    if (index < posts.lastIndex) {
                        HorizontalDivider(thickness = 1.dp, color = colorGrey100())
                    }
                }
            }
        }
    } else {
        Column {
            repeat(10) {
                PostShimmer()
            }
        }
    }
}
