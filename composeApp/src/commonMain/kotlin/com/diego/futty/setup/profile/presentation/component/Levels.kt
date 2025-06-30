package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressBar
import com.diego.futty.home.design.presentation.component.progressbar.LinearProgressBar

@Composable
fun Levels() {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = "Niveles",
            style = typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorGrey900()
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.width(8.dp))
            CircularProgressBar(progress = 0.2f, text = "Nivel 1")
            CircularProgressBar(progress = 0.4f, text = "Nivel 2", color = colorErrorLight())
            CircularProgressBar(progress = 0.6f, text = "Nivel 3", color = colorAlertLight())
            CircularProgressBar(progress = 0.8f, text = "Nivel 4", color = colorInfoLight())
            CircularProgressBar(progress = 1.0f, text = "Nivel 5", color = colorGrey700())
            Spacer(Modifier.width(8.dp))
        }
        LinearProgressBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            progress = 0.4f,
            color = colorGrey900()
        )
    }
}