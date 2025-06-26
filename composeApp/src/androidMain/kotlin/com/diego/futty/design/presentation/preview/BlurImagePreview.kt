package com.diego.futty.design.presentation.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.home.design.presentation.component.image.BlurredImage

@Preview
@Composable
private fun Preview() {
    FuttyTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BlurredImage(
                image = "",
                blur = 20.dp
            )
            BlurredImage(
                image = "",
                blur = 30.dp
            )
            BlurredImage(
                image = "",
                blur = 40.dp
            )
            BlurredImage(
                image = "",
                blur = 50.dp
            )
            BlurredImage(
                image = "",
                blur = 60.dp
            )
            BlurredImage(
                image = "",
                blur = 70.dp
            )
        }
    }
}
