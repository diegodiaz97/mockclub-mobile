package com.diego.futty.design.presentation.screen

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
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.design.presentation.component.banner.Banner
import com.diego.futty.design.presentation.component.banner.ScrollBanner
import com.diego.futty.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.design.presentation.component.flowrow.FlowList
import com.diego.futty.design.presentation.component.topbar.TopBar
import com.diego.futty.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.design.presentation.viewmodel.DesignViewModel
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource
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
                title = "Hola Diego!",
                topBarActionType = TopBarActionType.Profile(
                    initials = "DD",
                    tint = colorGrey0(),
                    background = colorGrey900(),
                    onClick = { viewModel.onProfilePressed() }
                )
            )
        },
        content = { paddingValues ->
            DesignContent(viewModel, paddingValues)
        },
    )
    BottomSheetContent(viewModel)
}

@Composable
fun BottomSheetContent(viewModel: DesignViewModel) {
    if (viewModel.bottomsheetDismissed.value.not()) {
        BottomSheet(
            onDismiss = { viewModel.onBottomSheetDismissed() },
            onAction = { viewModel.onBottomSheetDismissed() },
        )
    }
}

@Composable
fun DesignContent(viewModel: DesignViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FlowList(viewModel.chipItems.value, viewModel.selectedChip.value) { index ->
            viewModel.onChipSelected(index)
        }

        Banner.BorderBanner(
            title = "Dinero en cuenta:",
            subtitle = "$ 254.300,59 ARS\n\n$ 3.700 USD",
        ).Draw()

        Banner.SuccessBanner(
            title = "¡Listo!",
            subtitle = "Éste banner te avisará de situaciones positivas.",
        ).Draw()

        Banner.ErrorBanner(
            title = "Algo salió mal",
            subtitle = "Éste banner te avisará cuando algo salió mal.",
        ).Draw()

        Banner.AlertBanner(
            title = "¡Cuidado!",
            subtitle = "Éste banner te avisará cuando algo pueda salir mal.",
        ).Draw()

        Banner.InfoBanner(
            title = "¿Sabías?",
            subtitle = "Éste banner te puede mostrar información.",
        ).Draw()

        Banner.ClickableBanner(
            title = "Banner Accionable",
            subtitle = "Éste es un banner que se puede accionar.",
            onClick = { viewModel.onButtonPressed() }
        ).Draw()

        Banner.ClickableBanner(
            image = painterResource(Res.drawable.book_error_2),
            title = "Banner Accionable",
            subtitle = "Éste es un banner que se puede accionar.",
            onClick = { }
        ).Draw()

        val scrollableBanners = viewModel.getScrollableBanners()
        ScrollBanner(
            items = listOf(
                scrollableBanners,
                scrollableBanners.copy(
                    action = { viewModel.onScrollBannerPressed("accion girasoles") },
                    illustration = Res.drawable.girasoles
                ),
            ),
        )
    }
}

