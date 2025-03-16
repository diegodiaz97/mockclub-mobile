package com.diego.futty.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun FuttyTheme(
    palette: ColorScheme = if (isSystemInDarkTheme()) NightColorScheme else DayColorScheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = palette,
        typography = PoppinsTypography(),
        content = content
    )
}

internal val DayColorScheme = lightColorScheme(
    surface = Success,
    surfaceContainer = SuccessLight,
    onSurface = SuccessDark,

    error = Error,
    errorContainer = ErrorLight,
    onError = ErrorDark,

    surfaceDim = Alert,
    onSurfaceVariant = AlertLight,
    surfaceContainerHighest = AlertDark,

    inverseSurface = Info,
    inverseOnSurface = InfoLight,
    inversePrimary = InfoDark,

    background = Grey0,
    onBackground = Grey100,
    surfaceContainerLowest = Grey200,
    surfaceContainerLow = Grey300,
    surfaceContainerHigh = Grey400,
    surfaceBright = Grey500,
    surfaceVariant = Grey600,
    surfaceTint = Grey700,
    onErrorContainer = Grey800,
    outline = Grey900,
)

internal val NightColorScheme = darkColorScheme(
    surface = NightSuccess,
    surfaceContainer = NightSuccessLight,
    onSurface = NightSuccessDark,

    error = NightError,
    errorContainer = NightErrorLight,
    onError = NightErrorDark,

    surfaceDim = NightAlert,
    onSurfaceVariant = NightAlertLight,
    surfaceContainerHighest = NightAlertDark,

    inverseSurface = NightInfo,
    inverseOnSurface = NightInfoLight,
    inversePrimary = NightInfoDark,

    background = Grey900,
    onBackground = Grey800,
    surfaceContainerLowest = Grey700,
    surfaceContainerLow = Grey600,
    surfaceContainerHigh = Grey500,
    surfaceBright = Grey400,
    surfaceVariant = Grey300,
    surfaceTint = Grey200,
    onErrorContainer = Grey100,
    outline = Grey0,
)
