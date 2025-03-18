package com.diego.futty.authentication.welcome.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.diego.futty.core.presentation.theme.colorGrey0
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen() {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        content = { WelcomeContent() },
    )
}

@Composable
private fun WelcomeContent() {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(Res.drawable.girasoles),
        contentScale = ContentScale.Crop,
        contentDescription = "welcome"
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Futty",
            style = typography.displayMedium,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey0(),
        )
    }
}
