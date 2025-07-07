package com.diego.futty.home.design.presentation.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.banner.BannerType
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.banner.ScrollBanner
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.design.presentation.component.user.Draw
import com.diego.futty.home.design.presentation.viewmodel.DesignViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Menu2
import compose.icons.tablericons.Search
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesignScreen(
    viewModel: DesignViewModel = koinViewModel(),
) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Descubre",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.Menu2,
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
        UsersList(viewModel)
        MainBanner()
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
            onFocusChanged = { },
            onTextChangeAction = { viewModel.updateSearch(it) }
        ).Draw()
        Avatar.IconAvatar(
            icon = TablerIcons.Search,
            background = colorGrey0(),
            tint = colorGrey400()
        ).Draw()
    }
}

@Composable
fun MainBanner() {
    ScrollBanner(
        bannerType = BannerType.FullImage,
        items = listOf(
            BannerUIData(
                title = "Memes",
                description = "Mira las mejores imágenes estilo Ghibli!",
                illustration = "https://www.portafolio.co/files/article_new_multimedia/uploads/2025/03/31/67eac96494a70.jpeg",
                label = "Ver más",
                action = { },
            ),
            BannerUIData(
                title = "Música",
                description = "Nuevos detalles de la muerte de John Lennon.",
                illustration = "https://d3b5jqy5xuub7g.cloudfront.net/wp-content/uploads/2025/03/estudio-ghibili.jpg",
                label = "Ver más",
                action = { },
            ),
            BannerUIData(
                title = "Deportes",
                description = "Fotos inéditas de aquel maravilloso 18 de Diciembre.",
                illustration = "https://cdn.eldestapeweb.com/eldestape/032025/1743453153943.webp?cw=1500&ch=1000&extw=jpeg",
                label = "Ver más",
                action = { },
            ),
            BannerUIData(
                title = "Internacional",
                description = "Estas son las últimas noticias al rededor del mundo.",
                illustration = "https://i0.wp.com/www.lineadecontraste.com/wp-content/uploads/2025/03/WhatsApp-Image-2025-03-31-at-7.42.56-PM.jpeg?w=1200&ssl=1",
                label = "Ver más",
                action = { },
            ),
        ),
    )
}

@Composable
private fun UsersList(viewModel: DesignViewModel) {
    val users = viewModel.searchUsers.collectAsState().value
    val input = viewModel.searchText.collectAsState().value
    AnimatedVisibility(users.isNotEmpty() && input.isNotEmpty()) {
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
}