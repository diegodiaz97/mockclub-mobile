package com.diego.futty.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

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
