package com.diego.futty.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.random.Random

@Composable
internal fun colorSuccess() = MaterialTheme.colorScheme.surface

@Composable
internal fun colorSuccessLight() = MaterialTheme.colorScheme.surfaceContainer

@Composable
internal fun colorSuccessDark() = MaterialTheme.colorScheme.onSurface

@Composable
internal fun colorError() = MaterialTheme.colorScheme.error

@Composable
internal fun colorErrorLight() = MaterialTheme.colorScheme.errorContainer

@Composable
internal fun colorErrorDark() = MaterialTheme.colorScheme.onError

@Composable
internal fun colorAlert() = MaterialTheme.colorScheme.surfaceDim

@Composable
internal fun colorAlertLight() = MaterialTheme.colorScheme.onSurfaceVariant

@Composable
internal fun colorAlertDark() = MaterialTheme.colorScheme.surfaceContainerHighest

@Composable
internal fun colorInfo() = MaterialTheme.colorScheme.inverseSurface

@Composable
internal fun colorInfoLight() = MaterialTheme.colorScheme.inverseOnSurface

@Composable
internal fun colorInfoDark() = MaterialTheme.colorScheme.inversePrimary

@Composable
internal fun colorGrey0() = MaterialTheme.colorScheme.background

@Composable
internal fun colorGrey100() = MaterialTheme.colorScheme.onBackground

@Composable
internal fun colorGrey200() = MaterialTheme.colorScheme.surfaceContainerLowest

@Composable
internal fun colorGrey300() = MaterialTheme.colorScheme.surfaceContainerLow

@Composable
internal fun colorGrey400() = MaterialTheme.colorScheme.surfaceContainerHigh

@Composable
internal fun colorGrey500() = MaterialTheme.colorScheme.surfaceBright

@Composable
internal fun colorGrey600() = MaterialTheme.colorScheme.surfaceVariant

@Composable
internal fun colorGrey700() = MaterialTheme.colorScheme.surfaceTint

@Composable
internal fun colorGrey800() = MaterialTheme.colorScheme.onErrorContainer

@Composable
internal fun colorGrey900() = MaterialTheme.colorScheme.outline

fun Color.toHex(includeAlpha: Boolean = false): String {
    val colorInt = this.toArgb()
    val alpha = (colorInt shr 24) and 0xFF
    val red = (colorInt shr 16) and 0xFF
    val green = (colorInt shr 8) and 0xFF
    val blue = colorInt and 0xFF

    return buildString {
        append("#")
        if (includeAlpha) {
            append(alpha.toString(16).padStart(2, '0'))
        }
        append(red.toString(16).padStart(2, '0'))
        append(green.toString(16).padStart(2, '0'))
        append(blue.toString(16).padStart(2, '0'))
    }.uppercase()
}

fun String.toColor(): Color {
    val hex = this.removePrefix("#") // Elimina el "#" si está presente
    val parsedColor = when (hex.length) {
        6 -> hex.toLong(16) or 0x00000000FF000000 // Agrega alpha FF
        8 -> hex.toLong(16) // Ya incluye alpha
        else -> throw IllegalArgumentException("Hex inválido: $this")
    }
    return Color(parsedColor)
}

fun getRandomLightColorHex(): String {
    val colors = listOf("0xFF71D88A", "0xFFF28B92", "0xFFFFE17A", "0xFF69D2E7")
    val index = Random.nextInt(4)

    return colors[index]
}

@Composable
fun GetRandomLightColor(): Color {
    val colors = listOf(colorSuccessLight(), colorErrorLight(), colorAlertLight(), colorInfoLight())
    val index = Random.nextInt(4)

    return colors[index]
}
