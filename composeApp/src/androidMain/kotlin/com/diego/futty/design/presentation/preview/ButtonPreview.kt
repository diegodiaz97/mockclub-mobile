package com.diego.futty.design.presentation.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton

@Preview
@Composable
private fun PrimaryButtonEnabled() {
    FuttyTheme {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            title = "Aceptar",
            isEnabled = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonDisabled() {
    FuttyTheme {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            title = "Aceptar",
            isEnabled = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonEnabled() {
    FuttyTheme {
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            title = "Cancelar",
            isEnabled = true,
            onClick = { }
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonDisabled() {
    FuttyTheme {
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            title = "Cancelar",
            isEnabled = false,
            onClick = { }
        )
    }
}
