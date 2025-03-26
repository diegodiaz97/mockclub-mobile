package com.diego.futty.home.design.presentation.component.progressbar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorSuccessLight

@Composable
fun LinearProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color = colorSuccessLight()
) {
    var progressIndicator by remember { mutableStateOf(0F) }
    val progressAnimDuration = 1_500
    val progressAnimation by animateFloatAsState(
        targetValue = progressIndicator,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
    )
    LaunchedEffect(true) {
        progressIndicator = progress
    }

    LinearProgressIndicator(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .height(12.dp)
            .fillMaxSize(),
        progress = { progressAnimation }, /* 0.0f a 1.0f*/
        color = color,
        trackColor = colorGrey100(),
        gapSize = (-15).dp,
        strokeCap = StrokeCap.Round
    ) { }
}

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    text: String = "",
    color: Color = colorSuccessLight()
) {
    var progressIndicator by remember { mutableStateOf(0F) }
    val progressAnimDuration = 1_500
    val progressAnimation by animateFloatAsState(
        targetValue = progressIndicator,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
    )
    LaunchedEffect(true) {
        progressIndicator = progress
    }

    Box {
        CircularProgressIndicator(
            modifier = modifier.size(90.dp),
            progress = { progressAnimation }, /* 0.0f a 1.0f*/
            color = color,
            trackColor = colorGrey100(),
            gapSize = (-15).dp,
            strokeCap = StrokeCap.Round,
            strokeWidth = 12.dp,
        )
        Text(
            modifier = modifier.align(Alignment.Center),
            text = text,
            style = typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = colorGrey900()
        )
    }
}
