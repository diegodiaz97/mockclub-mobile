package com.diego.futty.home.feed.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorPrimary
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.progressbar.DynamicLinearProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.presentation.component.Keys
import com.diego.futty.home.feed.presentation.component.OpenedImage
import com.diego.futty.home.feed.presentation.component.RevealOverlayContent
import com.diego.futty.home.feed.presentation.component.TopBanners
import com.diego.futty.home.feed.presentation.component.showNextReveal
import com.diego.futty.home.feed.presentation.component.tryShowReveal
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import com.diego.futty.setup.profile.presentation.component.PostsList
import com.svenjacobs.reveal.Reveal
import com.svenjacobs.reveal.RevealCanvasState
import com.svenjacobs.reveal.RevealShape
import com.svenjacobs.reveal.RevealState
import com.svenjacobs.reveal.revealable
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
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
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = viewModel.isRefreshing.value,
                    onRefresh = { viewModel.onFeedRefreshed() }
                )

                DragRefreshLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    state = pullRefreshState
                ) {
                    FeedContent(viewModel, scope, revealState)
                }
            },
        )
        OpenedImage(
            images = viewModel.openedImage.value,
            index = viewModel.openedImageIndex.value,
            aspectRatio = viewModel.openedImageRatio.value,
        ) { viewModel.onImageClosed() }

        viewModel.modal.value?.Draw()
    }
}

@Composable
private fun FeedContent(
    viewModel: FeedViewModel,
    scope: CoroutineScope,
    revealState: RevealState,
) {
    Box {
        Posts(viewModel, scope, revealState)
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
            background = colorPrimary(),
            tint = Grey0,
            onClick = { viewModel.showPostCreation() }
        ).Draw()
    }
}

@Composable
private fun Posts(
    viewModel: FeedViewModel,
    scope: CoroutineScope,
    revealState: RevealState,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PostsList(
            posts = viewModel.posts.value,
            topListComponents = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TopBanners(scope, revealState) {
                        viewModel.onChallengesClicked()
                    }
                    LoadingProgress(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Posteando",
                        progress = viewModel.postCreationProgress.value
                    )
                }
            },
            onPostClicked = { post ->
                viewModel.onPostClicked(post)
            },
            onLikeClicked = { post ->
                viewModel.onLikeClicked(post)
            },
            onImageClicked = { images, index, ratio ->
                viewModel.onImageClicked(images, index, ratio)
            },
            onScrolled = {
                viewModel.fetchFeed()
            }
        )
    }
}

@Composable
fun LoadingProgress(
    modifier: Modifier = Modifier,
    text: String?,
    progress: Float
) {
    AnimatedVisibility(
        visible = progress > 0f && progress <= 1,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colorGrey100())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (text != null) {
                Text(
                    text = text,
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = colorGrey900()
                )
            }
            DynamicLinearProgressBar(
                progress = progress,
                color = colorGrey900()
            )
        }
    }
}
