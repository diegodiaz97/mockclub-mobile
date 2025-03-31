package com.diego.futty.authentication.profileCreation.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.authentication.profileCreation.presentation.viewmodel.ProfileCreationViewModel
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.HideKeyboard
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import compose.icons.TablerIcons
import compose.icons.tablericons.X

@Composable
fun ProfileCreationScreen(viewModel: ProfileCreationViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize().clickable { viewModel.hideKeyboard() },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Crear Perfil",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.X,
                    onClick = { viewModel.onCloseClicked() }
                )
            )
        },
        content = { paddingValues ->
            ProfileCreationContent(viewModel, paddingValues)
        },
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding()) {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    title = "Crear cuenta",
                    isEnabled = viewModel.canContinue.value,
                    onClick = { viewModel.onContinueClicked() }
                )
            }
        }
    )
}

@Composable
private fun ProfileCreationContent(
    viewModel: ProfileCreationViewModel,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { viewModel.hideKeyboard() }
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Avatar.ProfileAvatar(
            imageUrl = viewModel.image.value,
            initials = viewModel.initials.value,
            background = viewModel.background.value.toColor(),
            avatarSize = AvatarSize.Extra
        ).Draw()

        TextInput.Input(
            input = viewModel.name.value,
            label = "Nombre",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateName(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.lastName.value,
            label = "Apellido",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateLastName(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.description.value,
            label = "Description (opcional)",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateDescription(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.country.value,
            label = "Nacionalidad",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateCountry(it) }
        ).Draw()

        AnimatedVisibility(viewModel.banner.value != null) {
            viewModel.banner.value?.Draw()
        }
    }

    if (viewModel.hideKeyboard.value) {
        HideKeyboard()
    }
}
