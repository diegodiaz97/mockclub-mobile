package com.diego.futty.design.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.Alert
import com.diego.futty.core.presentation.AlertDark
import com.diego.futty.core.presentation.AlertLight
import com.diego.futty.core.presentation.Error
import com.diego.futty.core.presentation.ErrorDark
import com.diego.futty.core.presentation.ErrorLight
import com.diego.futty.core.presentation.FuttyTheme
import com.diego.futty.core.presentation.Grey0
import com.diego.futty.core.presentation.Grey100
import com.diego.futty.core.presentation.Grey200
import com.diego.futty.core.presentation.Grey300
import com.diego.futty.core.presentation.Grey400
import com.diego.futty.core.presentation.Grey500
import com.diego.futty.core.presentation.Grey600
import com.diego.futty.core.presentation.Grey700
import com.diego.futty.core.presentation.Grey800
import com.diego.futty.core.presentation.Grey900
import com.diego.futty.core.presentation.Info
import com.diego.futty.core.presentation.InfoDark
import com.diego.futty.core.presentation.InfoLight
import com.diego.futty.core.presentation.Success
import com.diego.futty.core.presentation.SuccessDark
import com.diego.futty.core.presentation.SuccessLight
import com.diego.futty.design.presentation.component.ColorPalette

@Preview
@Composable
private fun TopBarStates() {
    Column {
        ColorPalette(title = "SuccessLight", background = SuccessLight)
        ColorPalette(title = "Success", background = Success)
        ColorPalette(title = "SuccessDark", background = SuccessDark)

        ColorPalette(title = "ErrorLight", background = ErrorLight)
        ColorPalette(title = "Error", background = Error)
        ColorPalette(title = "ErrorDark", background = ErrorDark)

        ColorPalette(title = "AlertLight", background = AlertLight)
        ColorPalette(title = "Alert", background = Alert)
        ColorPalette(title = "AlertDark", background = AlertDark)

        ColorPalette(title = "InfoLight", background = InfoLight)
        ColorPalette(title = "Info", background = Info)
        ColorPalette(title = "InfoDark", background = InfoDark)
    }
}

@Preview
@Composable
private fun TopBarGreyScale() {
    FuttyTheme {
        Column {
            ColorPalette(title = "Grey0", background = Grey0)
            ColorPalette(title = "Grey100", background = Grey100)
            ColorPalette(title = "Grey200", background = Grey200)
            ColorPalette(title = "Grey300", background = Grey300)
            ColorPalette(title = "Grey400", background = Grey400)
            ColorPalette(title = "Grey500", background = Grey500)
            ColorPalette(title = "Grey600", background = Grey600)
            ColorPalette(title = "Grey700", background = Grey700)
            ColorPalette(title = "Grey800", background = Grey800)
            ColorPalette(title = "Grey900", background = Grey900)
        }
    }
}
