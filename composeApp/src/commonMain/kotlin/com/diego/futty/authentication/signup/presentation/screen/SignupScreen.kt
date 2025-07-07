package com.diego.futty.authentication.signup.presentation.screen

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
import com.diego.futty.authentication.signup.presentation.viewmodel.SignupViewModel
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.utils.HideKeyboard
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType

@Composable
fun SignupScreen(viewModel: SignupViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize().clickable { viewModel.hideKeyboard() },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Crear cuenta",
                topBarActionType = TopBarActionType.None,
                onBack = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            SignupContent(viewModel, paddingValues)
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
                    title = "Crear cuenta",
                    isEnabled = viewModel.canCreateAccount.value,
                    onClick = { viewModel.onSignupClicked() }
                )
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Ya tengo cuenta",
                    onClick = { viewModel.onBackClicked() }
                )
            }
        }
    )
}

@Composable
private fun SignupContent(viewModel: SignupViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { viewModel.hideKeyboard() }
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        TextInput.Input(
            input = viewModel.email.value,
            label = "Email",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateEmail(it) }
        ).Draw()

        TextInput.PasswordInput(
            input = viewModel.password.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updatePassword(it) }
        ).Draw()

        TextInput.PasswordInput(
            input = viewModel.confirmPassword.value,
            label = "Repetir contraseña",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateConfirmPassword(it) }
        ).Draw()

        AnimatedVisibility(viewModel.banner.value != null) {
            viewModel.banner.value?.Draw()
        }
    }
    if (viewModel.hideKeyboard.value) {
        HideKeyboard()
    }
}
