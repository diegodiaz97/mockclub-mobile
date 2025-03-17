package com.diego.futty.authentication.login.presentation.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.authentication.login.presentation.viewmodel.LoginViewModel
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.design.presentation.component.button.PrimaryButton
import com.diego.futty.design.presentation.component.button.SecondaryButton
import com.diego.futty.design.presentation.component.input.TextInput
import com.diego.futty.design.presentation.component.topbar.TopBar
import com.diego.futty.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.design.utils.HideKeyboard

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize().clickable { viewModel.hideKeyboard() },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Bienvenido!",
                topBarActionType = TopBarActionType.None
            )
        },
        content = { paddingValues ->
            LoginContent(viewModel, paddingValues)
        },
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding()) {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    title = "Iniciar sesión",
                    isEnabled = viewModel.canLogin.value,
                    onClick = { viewModel.onLoginClicked() }
                )
                SecondaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
            .clickable { viewModel.hideKeyboard() }
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextInput.MailInput(
            viewModel.email.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateEmail(it) }
        ).Draw()

        TextInput.PasswordInput(
            viewModel.password.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updatePassword(it) }
        ).Draw()

        AnimatedVisibility(viewModel.banner.value != null) {
            viewModel.banner.value?.Draw()
        }
    }
    if (viewModel.hideKeyboard.value) {
        HideKeyboard()
    }
}
