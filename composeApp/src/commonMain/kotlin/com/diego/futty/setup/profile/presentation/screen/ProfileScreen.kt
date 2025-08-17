package com.diego.futty.setup.profile.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.GearSix
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorInfo
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.profile.presentation.component.DesiresList
import com.diego.futty.setup.profile.presentation.component.Levels
import com.diego.futty.setup.profile.presentation.component.PostsGrid
import com.diego.futty.setup.profile.presentation.component.ProfileImageHandler
import com.diego.futty.setup.profile.presentation.component.UpgradeBanner
import com.diego.futty.setup.profile.presentation.component.UserMainInfo
import com.diego.futty.setup.profile.presentation.component.UserSecondaryInfo
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Perfil",
                topBarActionType = TopBarActionType.Icon(
                    icon = PhosphorIcons.Bold.GearSix,
                    onClick = { viewModel.onSettingsClicked() }
                ),
                onBack = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            ProfileContent(viewModel, paddingValues)
        },
    )
    ProfileImageHandler(viewModel)
}

@Composable
private fun ProfileContent(viewModel: ProfileViewModel, paddingValues: PaddingValues) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing.value,
        onRefresh = { viewModel.onFeedRefreshed() }
    )

    DragRefreshLayout(
        modifier = Modifier.fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        state = pullRefreshState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Posts(viewModel)
            viewModel.modal.value?.Draw()
        }
    }
}

@Composable
private fun FollowButton(viewModel: ProfileViewModel) {
    AnimatedVisibility(
        visible = viewModel.showFollowButton.value,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            color = if (viewModel.followingUser.value) {
                colorError()
            } else {
                colorInfo()
            },
            title = if (viewModel.followingUser.value) {
                "Dejar de seguir"
            } else {
                "Seguir"
            },
            onClick = { viewModel.onFollowOrUnfollowClicked() }
        )
    }
}

@Composable
private fun Posts(viewModel: ProfileViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Parte superior: header, stats, botón seguir, etc.
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserMainInfo(viewModel)
                UserSecondaryInfo(viewModel)
                UpgradeBanner(viewModel)
                FollowButton(viewModel)
                HorizontalDivider(thickness = 12.dp, color = colorGrey0())
            }
        }

        // Grilla de posts (en un solo item)
        item {
            PostsGrid(
                posts = viewModel.posts.value,
                onPostClicked = { post ->
                    viewModel.onPostClicked(post)
                },
                onScrolled = {
                    viewModel.fetchOwnFeed()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DesiresList(viewModel)
                Levels()
            }
        }
    }
}
