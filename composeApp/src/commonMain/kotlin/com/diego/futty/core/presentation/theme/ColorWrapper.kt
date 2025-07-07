package com.diego.futty.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
internal fun colorPrimary() = MaterialTheme.colorScheme.primary

@Composable
internal fun colorSecondary() = MaterialTheme.colorScheme.secondary

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

@Composable
fun String.toColor(): Color {
    val hex = this.trim().removePrefix("#").removePrefix("0x")

    if (hex.isEmpty() || hex.any { !it.isDigit() && it !in 'A'..'F' && it !in 'a'..'f' }) {
        println("⚠️ Error: String inválido en toColor(): '$this'")
        return Color(0xFFFFFFFF) // Blanco como fallback
    }

    val parsedColor = when (hex.length) {
        6 -> hex.toLong(16) or 0xFF000000 // Agrega alpha FF
        8 -> hex.toLong(16) // Ya incluye alpha
        else -> {
            println("⚠️ Error: Longitud inválida en toColor(): '$this'")
            0xFFFFFFFF // Fallback
        }
    }
    return Color(parsedColor)
}

fun getRandomLightColorHex(): String {
    val colors = listOf("0xFF71D88A", "0xFFF28B92", "0xFFFFE17A", "0xFF69D2E7")
    val index = Random.nextInt(4)

    return colors[index]
}
