package com.diego.futty.authentication.login.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import com.diego.futty.authentication.welcome.presentation.viewmodel.WelcomeViewModel
import com.diego.futty.core.data.firebase.FirebaseManager.passage
import com.diego.futty.core.presentation.theme.Grey900
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfo
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Apple
import compose.icons.fontawesomeicons.brands.Google
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.background
import futty.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    passage.bindToView()
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Box(Modifier.fillMaxSize()) {
                WelcomeContent(Modifier)
                ButtonsContent(
                    Modifier.align(Alignment.BottomCenter),
                    viewModel
                )
            }
            viewModel.modal.value?.Draw()
        },
    )
}

@Composable
private fun WelcomeContent(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(
                color = Grey900.copy(alpha = 0.4f),
                blendMode = BlendMode.Darken
            ),
            contentScale = ContentScale.FillHeight,
            contentDescription = "welcome image",
            painter = painterResource(Res.drawable.background),
        )
    }
}

@Composable
private fun ButtonsContent(modifier: Modifier, viewModel: WelcomeViewModel) {
    Column {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(240.dp)
                .weight(1f)
                .wrapContentSize(Alignment.Center),
            painter = painterResource(Res.drawable.icon),
            contentDescription = "app icon"
        )

        Column(
            modifier = modifier
                .weight(1f)
                .clip(RoundedCornerShape(36.dp, 36.dp))
                .background(colorGrey0())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Te damos la bienvenida",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorGrey900()
            )

            Text(
                text = "Inspírate y comparte tus diseños\ncon usuarios de todo el mundo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center,
                style = typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey600()
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Continuar con Google",
                icon = FontAwesomeIcons.Brands.Google,
                onClick = { viewModel.onLoginWithGoogleClicked() }
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Continuar con Apple",
                icon = FontAwesomeIcons.Brands.Apple,
                onClick = { viewModel.onLoginWithAppleClicked() }
            )

            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Continuar con email",
                color = colorInfo(),
                onClick = { viewModel.onLoginWithEmailClicked() }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = colorGrey200()
                )
                Text(
                    text = "¿No tienes cuenta?",
                    textAlign = TextAlign.Center,
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = colorGrey600()
                )
                SecondaryButton(
                    title = "Regístrate",
                    onClick = { viewModel.onSignupClicked() }
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = colorGrey200()
                )
            }

        }
    }
}
