package com.diego.futty.authentication.login.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.authentication.login.presentation.viewmodel.LoginViewModel
import com.diego.futty.core.data.firebase.FirebaseManager.passage
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorSuccess
import com.diego.futty.core.presentation.utils.HideKeyboard
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.diego.futty.home.design.presentation.component.input.TextInput

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    //if (PlatformInfo.isAndroid){
    passage.bindToView()
    //}
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize().clickable { viewModel.hideKeyboard() },
        content = { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize(),
            ) {
                LoginContent(
                    Modifier,
                    viewModel,
                )
                ButtonsContent(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-40).dp),
                    viewModel
                )
            }
            if (viewModel.hideKeyboard.value) {
                HideKeyboard()
            }
        },
    )
}

@Composable
private fun LoginContent(
    modifier: Modifier,
    viewModel: LoginViewModel,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { viewModel.hideKeyboard() },
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            colorFilter = ColorFilter.tint(
                color = Grey900.copy(alpha = 0.4f),
                blendMode = BlendMode.Darken
            ),
            contentScale = ContentScale.FillWidth,
            contentDescription = "login image",
            image = "https://i.pinimg.com/736x/29/ca/6c/29ca6cee428f5c11dbe08abfd5b42491.jpg"
        )

        AnimatedVisibility(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            visible = viewModel.banner.value != null,
        ) {
            viewModel.banner.value?.Draw()
        }
    }

}

@Composable
private fun ButtonsContent(modifier: Modifier, viewModel: LoginViewModel) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .clip(RoundedCornerShape(24.dp, 24.dp))
            .background(colorGrey0())
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Bienvenido a MockClub",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            style = typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )

        TextInput.Input(
            input = viewModel.email.value,
            label = "Email",
            placeholder = "ejemplo@gmail.com",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateEmail(it) }
        ).Draw()

        TextInput.PasswordInput(
            input = viewModel.password.value,
            placeholder = "Ejemplo123",
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updatePassword(it) }
        ).Draw()

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            title = "Iniciar sesión",
            isEnabled = viewModel.canLogin.value,
            onClick = { viewModel.onLoginClicked() }
        )
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            title = "Continuar con Google",
            color = colorSuccess(),
            isEnabled = true,
            onClick = { viewModel.onLoginWithGoogleClicked() }
        )
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            title = "Continuar con Apple",
            color = colorGrey900(),
            isEnabled = true,
            onClick = { viewModel.onLoginWithAppleClicked() }
        )
        SecondaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp),
            title = "Registrarme",
            isEnabled = true,
            onClick = { viewModel.onSignupClicked() }
        )
    }
}
