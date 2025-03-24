package com.diego.futty.home.match.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.maps.MapView
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorSuccessLight
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.banner.ScrollBanner
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.match.presentation.viewmodel.MatchViewModel
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.girasoles
import org.koin.compose.viewmodel.koinViewModel

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
                title = "Opina",
                topBarActionType = TopBarActionType.Button(text = "Ayuda", onClick = { onBack() })
            )
        },
        content = { paddingValues ->
            MatchV2Content(paddingValues)
        },
    )
}

@Composable
private fun MatchV2Content(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MapView()

        Banner.StatusBanner(
            title = "¿Sabías?",
            subtitle = "Éste banner te puede mostrar información.",
            status = BannerStatus.Error
        ).Draw()

        Banner.StatusBanner(
            title = "Dinero en cuenta:",
            subtitle = "$ 254.300,59 ARS\n\n$ 3.700 USD",
            status = BannerStatus.Border
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
