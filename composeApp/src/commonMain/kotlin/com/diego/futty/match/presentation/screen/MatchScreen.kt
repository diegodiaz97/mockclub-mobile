package com.diego.futty.match.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.AlertLight
import com.diego.futty.core.presentation.ErrorLight
import com.diego.futty.core.presentation.Grey0
import com.diego.futty.core.presentation.Grey100
import com.diego.futty.core.presentation.Grey900
import com.diego.futty.match.domain.model.match.LiveMatch
import com.diego.futty.design.presentation.component.button.PrimaryButton
import com.diego.futty.match.presentation.component.league.LeagueCard
import com.diego.futty.design.presentation.component.topbar.TopBar
import com.diego.futty.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.match.presentation.viewmodel.MatchViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.AlertCircle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchScreen(
    viewModel: MatchViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = Grey0,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "¡Hola Diego!",
                topBarActionType = TopBarActionType.Button(text = "Libros", onClick = { onBack() })
            )
        },
        content = { paddingValues ->
            MatchContent(viewModel, paddingValues)
        },
        bottomBar = {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                title = "Continuar",
                isEnabled = viewModel.buttonEnabled.value,
                onClick = { viewModel.onButtonPressed() }
            )
        }
    )
}

@Composable
fun MatchContent(viewModel: MatchViewModel, paddingValues: PaddingValues) {
    when (val liveScores = viewModel.liveScores.value) {
        null -> {
            // LOADER
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Section(ErrorLight, viewModel)
            }
        }

        emptyList<LiveMatch>() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Section(AlertLight, viewModel)
            }
        }

        else -> {
            val leagues = viewModel.leagues.value ?: emptyList()
            LeagueCard(
                leagues,
                liveScores,
                paddingValues
            )
        }
    }
}

@Composable
fun Section(color: Color, viewModel: MatchViewModel) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .background(Grey100.copy(alpha = 0.4f))
                .size(36.dp)
                .padding(8.dp),
            imageVector = TablerIcons.AlertCircle,
            tint = Grey900,
            contentDescription = ""
        )
        Text(
            modifier = Modifier.weight(1f),
            text = viewModel.error.value,
            textAlign = TextAlign.Center,
            style = typography.labelLarge,
            color = Grey900
        )
    }
}
