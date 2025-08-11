package com.diego.futty.design.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.home.design.presentation.component.input.TextInput

@Preview
@Composable
private fun DayPasswordTextInputPreview() {
    FuttyTheme {
        TextInput.PasswordInput(
            "Contraseña12",
            onTextChangeAction = { }
        ).Draw()
    }
}

@Preview
@Composable
private fun DayEmailTextInputPreview() {
    FuttyTheme {
        TextInput.Input(
            input = "dieggodiaz97@gmail.com",
            label = "Email",
            onTextChangeAction = { }
        ).Draw()
    }
}
