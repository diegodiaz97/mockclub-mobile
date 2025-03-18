package com.diego.futty.core.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.NightColorScheme
import com.diego.futty.core.presentation.theme.colorAlert
import com.diego.futty.core.presentation.theme.colorAlertDark
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorErrorDark
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey300
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey800
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfo
import com.diego.futty.core.presentation.theme.colorInfoDark
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.core.presentation.theme.colorSuccess
import com.diego.futty.core.presentation.theme.colorSuccessDark
import com.diego.futty.core.presentation.theme.colorSuccessLight

@Preview
@Composable
private fun StatesDay() {
    FuttyTheme {
        StateColors()
    }
}

@Preview
@Composable
private fun GreyScaleDay() {
    FuttyTheme {
        GreyScaleColors()
    }
}

@Preview
@Composable
private fun StatesNight() {
    FuttyTheme(NightColorScheme) {
        StateColors()
    }
}

@Preview
@Composable
private fun GreyScaleNight() {
    FuttyTheme(NightColorScheme) {
        GreyScaleColors()
    }
}

@Composable
private fun StateColors() {
    Column {
        ColorPalette(title = "SuccessLight", background = colorSuccessLight())
        ColorPalette(title = "Success", background = colorSuccess())
        ColorPalette(title = "SuccessDark", background = colorSuccessDark())

        ColorPalette(title = "ErrorLight", background = colorErrorLight())
        ColorPalette(title = "Error", background = colorError())
        ColorPalette(title = "ErrorDark", background = colorErrorDark())

        ColorPalette(title = "AlertLight", background = colorAlertLight())
        ColorPalette(title = "Alert", background = colorAlert())
        ColorPalette(title = "AlertDark", background = colorAlertDark())

        ColorPalette(title = "InfoLight", background = colorInfoLight())
        ColorPalette(title = "Info", background = colorInfo())
        ColorPalette(title = "InfoDark", background = colorInfoDark())
    }
}

@Composable
private fun GreyScaleColors() {
    Column {
        ColorPalette(title = "Grey0", background = colorGrey0())
        ColorPalette(title = "Grey100", background = colorGrey100())
        ColorPalette(title = "Grey200", background = colorGrey200())
        ColorPalette(title = "Grey300", background = colorGrey300())
        ColorPalette(title = "Grey400", background = colorGrey400())
        ColorPalette(title = "Grey500", background = colorGrey500())
        ColorPalette(title = "Grey600", background = colorGrey600())
        ColorPalette(title = "Grey700", background = colorGrey700())
        ColorPalette(title = "Grey800", background = colorGrey800())
        ColorPalette(title = "Grey900", background = colorGrey900())
    }
}

@Composable
fun ColorPalette(title: String, background: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = background)
            .padding(vertical = 12.dp, horizontal = 16.dp),
    ) {
        Text(
            text = title,
            style = typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )
    }
}
