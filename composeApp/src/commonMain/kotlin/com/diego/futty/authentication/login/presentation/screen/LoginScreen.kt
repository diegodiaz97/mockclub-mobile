package com.diego.futty.authentication.login.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.diego.futty.authentication.login.presentation.viewmodel.LoginViewModel
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Iniciar sesión",
                topBarActionType = TopBarActionType.None,
                onBack = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            LoginContent(viewModel, paddingValues)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Iniciar sesión",
                    isEnabled = viewModel.canLogin.value,
                    onClick = { viewModel.onLoginClicked() }
                )
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Registrarme",
                    isEnabled = true,
                    onClick = { viewModel.onSignupClicked() }
                )
            }
        }
    )
}

@Composable
private fun LoginContent(viewModel: LoginViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        TextInput.Input(
            input = viewModel.email.value,
            label = "Email",
            onTextChangeAction = { viewModel.updateEmail(it) }
        ).Draw()

        TextInput.PasswordInput(
            input = viewModel.password.value,
            onTextChangeAction = { viewModel.updatePassword(it) }
        ).Draw()

        AnimatedVisibility(viewModel.banner.value != null) {
            viewModel.banner.value?.Draw()
        }
    }
}
