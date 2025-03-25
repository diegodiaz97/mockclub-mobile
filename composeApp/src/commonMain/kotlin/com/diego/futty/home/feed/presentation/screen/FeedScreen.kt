package com.diego.futty.home.feed.presentation.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.post.Post
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressBar
import com.diego.futty.home.design.presentation.component.progressbar.LinearProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Menu2
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import futty.composeapp.generated.resources.compose_multiplatform
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource
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
                    onClick = { viewModel.onProfilePressed() }
                )
            )
        },
        content = { paddingValues ->
            FeedContent(viewModel, paddingValues)
        },
    )
}

@Composable
private fun FeedContent(viewModel: FeedViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressBar(progress = 0.2f, text = "Nivel 1")
            CircularProgressBar(progress = 0.4f, text = "Nivel 2", color = colorErrorLight())
            CircularProgressBar(progress = 0.6f, text = "Nivel 3", color = colorAlertLight())
            CircularProgressBar(progress = 0.8f, text = "Nivel 4", color = colorInfoLight())
            CircularProgressBar(progress = 1.0f, text = "Nivel 5", color = colorGrey700())
        }
        LinearProgressBar(progress = 0.4f, color = colorGrey900())
        Post(
            profileImage = InitialsAvatar(initials = "DD"),
            name = "Diego Díaz",
            date = "25 de enero a las 12:32",
            text = "Estoy aprendiendo Jetpack Compose \uD83D\uDEAC \uD83D\uDE0E",
            image = painterResource(Res.drawable.compose_multiplatform),
        )
        Post(
            profileImage = InitialsAvatar(initials = "CF"),
            name = "Carolina Fubel",
            date = "23 de enero a las 18:43",
            text = "Hoy vi unos girasoles muy amarilloss",
            image = painterResource(Res.drawable.girasoles),
        )
        Post(
            profileImage = InitialsAvatar(initials = "DD"),
            name = "Diego Díaz",
            date = "22 de enero a las 13:21",
            text = "Que ganas de comer helado",
        )
        Post(
            profileImage = ImageAvatar(),
            name = "Davo Xeneize",
            date = "18 de enero a las 18:12",
            text = "Ojalá Paredes le haga un gol a Brasil",
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ImageAvatar() = Avatar.FullImageAvatar(
    image = painterResource(Res.drawable.book_error_2),
)

@Composable
private fun InitialsAvatar(initials: String) = Avatar.InitialsAvatar(
    initials = initials,
    tint = colorGrey0(),
    background = colorGrey900(),
)
