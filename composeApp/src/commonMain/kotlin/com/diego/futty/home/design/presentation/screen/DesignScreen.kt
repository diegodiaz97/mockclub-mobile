package com.diego.futty.home.design.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.List
import com.adamglin.phosphoricons.bold.MagnifyingGlass
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.home.design.presentation.component.emptystate.SimpleEmptyState
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.selector.SegmentControl
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.design.presentation.component.user.Draw
import com.diego.futty.home.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.setup.profile.presentation.component.PostsGrid
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesignScreen(
    viewModel: DesignViewModel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Descubre",
                topBarActionType = TopBarActionType.Icon(
                    icon = PhosphorIcons.Bold.List,
                    onClick = { viewModel.onProfileClicked() }
                )
            )
        },
        content = { paddingValues ->
            DesignContent(viewModel, paddingValues)
        },
    )
}

@Composable
private fun DesignContent(viewModel: DesignViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SearchInput(viewModel)
        SegmentControl(
            modifier = Modifier.fillMaxWidth(),
            segmentOptions = viewModel.searchTypes.value,
            selectedIndex = viewModel.selectedSearchType.value,
            onItemSelected = { viewModel.updateSearchType(it) }
        )
        if (viewModel.selectedSearchType.value == 0) {
            UsersList(viewModel)
        } else {
            PostsList(viewModel)
        }
    }
}

@Composable
fun SearchInput(viewModel: DesignViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextInput.Input(
            modifier = Modifier.weight(1f),
            input = viewModel.searchText.collectAsState().value,
            placeholder = "Buscar",
            leadingIcon = PhosphorIcons.Bold.MagnifyingGlass,
            onTextChangeAction = { viewModel.updateSearch(it) }
        ).Draw()
    }
}

@Composable
private fun UsersList(viewModel: DesignViewModel) {
    val users = viewModel.searchUsers.collectAsState().value
    val input = viewModel.searchText.collectAsState().value
    if (users != null) {
        AnimatedVisibility(
            visible = users.isNotEmpty() && input.isNotEmpty(),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                users.forEachIndexed { index, user ->
                    item {
                        user.Draw { viewModel.onUserClicked(user) }
                        if (index < users.lastIndex) {
                            HorizontalDivider(thickness = 1.dp, color = colorGrey100())
                        }
                    }
                }
            }
        }

        if (users.isEmpty() && input.isNotEmpty()) {
            SimpleEmptyState(
                modifier = Modifier.fillMaxSize(),
                text = "No se encontraron usuarios",
            )
        }
    }
}

@Composable
private fun PostsList(viewModel: DesignViewModel) {
    val posts = viewModel.searchPosts.collectAsState().value
    val input = viewModel.searchText.collectAsState().value
    if (posts != null) {
        AnimatedVisibility(
            visible = posts.isNotEmpty() && input.isNotEmpty(),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            PostsGrid(
                posts = viewModel.searchPosts.value,
                onPostClicked = { post ->
                    viewModel.onPostClicked(post)
                },
                onScrolled = {
                    //viewModel.fetchOwnFeed()
                }
            )
        }
        if (posts.isEmpty() && input.isNotEmpty()) {
            SimpleEmptyState(
                modifier = Modifier.fillMaxSize(),
                text = "No se encontraron posts",
            )
        }
    }
}
