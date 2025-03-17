package com.diego.futty.match.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.diego.futty.core.presentation.theme.AlertLight
import com.diego.futty.core.presentation.theme.ErrorLight
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey100
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorAlert
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorInfo
import com.diego.futty.core.presentation.theme.colorSuccess
import com.diego.futty.core.presentation.theme.colorSuccessLight
import com.diego.futty.design.presentation.component.avatar.Avatar
import com.diego.futty.design.presentation.component.avatar.AvatarSize
import com.diego.futty.design.presentation.component.banner.Banner
import com.diego.futty.design.presentation.component.banner.BannerUIData
import com.diego.futty.design.presentation.component.banner.ScrollBanner
import com.diego.futty.design.presentation.component.topbar.TopBar
import com.diego.futty.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.match.domain.model.match.LiveMatch
import com.diego.futty.match.presentation.component.league.LeagueCard
import com.diego.futty.match.presentation.viewmodel.MatchViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.AlertCircle
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.girasoles
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random

@Composable
fun MatchScreen(
    viewModel: MatchViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Perfil",
                topBarActionType = TopBarActionType.Button(text = "Ayuda", onClick = { onBack() })
            )
        },
        content = { paddingValues ->
            //MatchContent(viewModel, paddingValues)
            MatchV2Content(paddingValues)
        },
        /*bottomBar = {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                title = "Continuar",
                isEnabled = viewModel.buttonEnabled.value,
                onClick = { viewModel.onButtonPressed() }
            )
        }*/
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
                    .padding(top = paddingValues.calculateTopPadding())
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchV2Content(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 5,
        ) {
            initials.forEach { initial ->
                Avatar.InitialsAvatar(
                    initials = initial,
                    tint = Grey0,
                    background = getRandomColor(),
                    avatarSize = AvatarSize.Big
                ).Draw()
            }
        }

        Banner.ErrorBanner(
            title = "¿Sabías?",
            subtitle = "Éste banner te puede mostrar información.",
        ).Draw()

        Banner.BorderBanner(
            title = "Dinero en cuenta:",
            subtitle = "$ 254.300,59 ARS\n\n$ 3.700 USD",
        ).Draw()

        ScrollBanner(
            items = listOf(
                BannerUIData(
                    title = "Girasoles",
                    description = "Éste banner puede varias cosas en un mismo lugar. Máximo 2 líneas",
                    labelAction = "Ver más",
                    illustration = Res.drawable.girasoles,
                    action = { },
                ),
                BannerUIData(
                    title = "Título",
                    color = colorSuccessLight(),
                    description = "Éste banner puede varias cosas en un mismo lugar. Máximo 2 líneas",
                    labelAction = "Ver más",
                    action = { },
                ),
            )
        )
    }
}

private val initials = listOf("CM", "DC", "JC", "PD", "AT", "CF", "DD", "CH", "AP")

@Composable
private fun getRandomColor(): Color {
    val colors = listOf(colorSuccess(), colorError(), colorAlert(), colorInfo())
    val index = Random.nextInt(4)

    return colors[index]
}
