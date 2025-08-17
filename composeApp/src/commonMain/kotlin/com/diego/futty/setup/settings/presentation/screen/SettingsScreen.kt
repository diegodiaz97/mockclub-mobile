package com.diego.futty.setup.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Bell
import com.adamglin.phosphoricons.bold.Download
import com.adamglin.phosphoricons.bold.Lightning
import com.adamglin.phosphoricons.bold.QuestionMark
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.settings.presentation.viewmodel.SettingsViewModel
import compose.icons.Octicons
import compose.icons.octicons.Verified16

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Configuración",
                topBarActionType = TopBarActionType.Icon(
                    icon = viewModel.topBarIcon.value,
                    onClick = { viewModel.onChangeThemeClicked() }
                ),
                onBack = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            SettingsContent(viewModel, paddingValues)
        },
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding()) {
                SecondaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    title = "Cerrar sesión",
                    color = colorError(),
                    isEnabled = true,
                    onClick = { viewModel.onLogoutClicked() }
                )
            }
        }
    )
}

@Composable
private fun SettingsContent(viewModel: SettingsViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Banner.DisplayBanner(
            bannerUIData = BannerUIData(
                title = "Notificaciones",
                description = "Configuración de notificaciones.",
                icon = PhosphorIcons.Bold.Bell,
                size = AvatarSize.Small,
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()

        Banner.DisplayBanner(
            bannerUIData = BannerUIData(
                title = "Verificar",
                description = "Obten el verificado, justo a un gran conjunto de beneficios.",
                icon = Octicons.Verified16,
                size = AvatarSize.Small,
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()

        Banner.DisplayBanner(
            bannerUIData = BannerUIData(
                title = "Publicidad",
                description = "¡Publicita lo que quieras con nosotros!",
                icon = PhosphorIcons.Bold.Lightning,
                size = AvatarSize.Small,
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()

        Banner.DisplayBanner(
            bannerUIData = BannerUIData(
                title = "Ayuda",
                description = "Resuelve tus dudas.",
                icon = PhosphorIcons.Bold.QuestionMark,
                size = AvatarSize.Small,
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()

        Banner.DisplayBanner(
            bannerUIData = BannerUIData(
                title = "Guardado",
                description = "Mira las publicaciones que has guardado.",
                icon = PhosphorIcons.Bold.Download,
                size = AvatarSize.Small,
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()
    }
}
