package com.diego.futty.setup.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.settings.presentation.viewmodel.SettingsViewModel
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource

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
        Banner.ClickableBanner(
            title = "Notificaciones",
            subtitle = "Configuración de notificaciones.",
            onClick = { }
        ).Draw()
        Banner.ClickableBanner(
            title = "Verificar",
            subtitle = "Obten el verificado, justo a un gran conjunto de beneficios.",
            onClick = { }
        ).Draw()
        Banner.ClickableBanner(
            title = "Publicidad",
            subtitle = "¡Publicita lo que quieras con nosotros!",
            onClick = { }
        ).Draw()
        Banner.ClickableBanner(
            title = "Ayuda",
            subtitle = "Resuelve tus dudas.",
            onClick = { }
        ).Draw()
        Banner.ClickableBanner(
            title = "Guardado",
            subtitle = "Mira las publicaciones que has guardado.",
            onClick = { }
        ).Draw()
        Banner.ClickableBanner(
            image = painterResource(Res.drawable.girasoles),
            title = "Diego Díaz",
            subtitle = "hace 4hs",
            onClick = { }
        ).Draw()
    }
}
