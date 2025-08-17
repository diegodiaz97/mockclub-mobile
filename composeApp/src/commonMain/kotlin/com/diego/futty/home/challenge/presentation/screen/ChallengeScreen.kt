package com.diego.futty.home.challenge.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.List
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.home.challenge.presentation.component.CurrentChallengeCard
import com.diego.futty.home.challenge.presentation.component.PastChallengeCard
import com.diego.futty.home.challenge.presentation.viewmodel.ChallengeViewModel
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChallengeScreen(
    viewModel: ChallengeViewModel = koinViewModel(),
) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Desafíos",
                topBarActionType = TopBarActionType.Icon(
                    icon = PhosphorIcons.Bold.List,
                    onClick = { }
                )
            )
        },
        content = { paddingValues ->
            ChallengeContent(viewModel, paddingValues)
        },
    )
}

@Composable
private fun ChallengeContent(viewModel: ChallengeViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val challenges = listOf(
            "Barcelona visitante naranja",
            "Chelsea con Puma",
            "España estilo retro"
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CurrentChallengeCard(
                    time = "2 días, 4 horas",
                    title = "Brasil Mundial 2026",
                    subtitle = "Participar",
                    participants = "983 participantes",
                    background = "https://static.vecteezy.com/system/resources/thumbnails/029/460/954/large/moving-abstract-blurred-background-abstract-pattern-smooth-moving-background-neon-gradient-background-free-video.jpg",
                )
                HorizontalDivider(thickness = 16.dp, color = colorGrey0())
            }
            challenges.forEachIndexed { index, challenge ->
                item {
                    PastChallengeCard(
                        title = challenge,
                        status = "Finalizado",
                        participants = "1.353 participantes",
                        background = null
                    )
                }
                item { HorizontalDivider(thickness = 16.dp, color = colorGrey0()) }
            }
        }
    }
}
