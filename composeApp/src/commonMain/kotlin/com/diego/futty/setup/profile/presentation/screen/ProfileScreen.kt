package com.diego.futty.setup.profile.presentation.screen

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
import com.diego.futty.home.design.presentation.component.image.BlurredImage
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Settings
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
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.Settings,
                    onClick = { viewModel.onSettingsClicked() }
                ),
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
        BlurredImage(
            image = painterResource(Res.drawable.girasoles),
            blur = 50.dp
        ) {

        }
    }
}
