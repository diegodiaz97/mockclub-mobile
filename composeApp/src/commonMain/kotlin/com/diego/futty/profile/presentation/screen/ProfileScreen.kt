package com.diego.futty.profile.presentation.screen

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
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.design.presentation.component.banner.Banner
import com.diego.futty.design.presentation.component.button.PrimaryButton
import com.diego.futty.design.presentation.component.image.BlurredImage
import com.diego.futty.design.presentation.component.topbar.TopBar
import com.diego.futty.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.profile.presentation.viewmodel.ProfileViewModel
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Perfil",
                topBarActionType = TopBarActionType.None,
                onBack = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            ProfileContent(viewModel, paddingValues)
        },
    )
}

@Composable
private fun ProfileContent(viewModel: ProfileViewModel, paddingValues: PaddingValues) {
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
            subtitle = "guarda publicaciones para verlas más tarde.",
            onClick = { }
        ).Draw()
        Banner.ClickableBanner(
            image = painterResource(Res.drawable.girasoles),
            title = "Diego Díaz",
            subtitle = "hace 4hs",
            onClick = { }
        ).Draw()
        BlurredImage(
            image = painterResource(Res.drawable.girasoles),
            blur = 50.dp
        )
        BlurredImage(
            image = painterResource(Res.drawable.girasoles),
            blur = 60.dp
        )
        BlurredImage(
            image = painterResource(Res.drawable.girasoles),
            blur = 70.dp
        )

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            title = "Cambiar tema",
            isEnabled = true,
            onClick = { viewModel.onChangeThemeClicked() }
        )
    }
}
