package com.diego.futty.core.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.NightColorScheme
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey900

@Preview
@Composable
private fun DayBigFontPreview() {
    FuttyTheme(DayColorScheme) {
        FontPreviewBig()
    }
}

@Preview
@Composable
private fun DaySmallFontPreview() {
    FuttyTheme(DayColorScheme) {
        FontPreviewSmall()
    }
}

@Preview
@Composable
private fun NightBigFontPreview() {
    FuttyTheme(NightColorScheme) {
        FontPreviewBig()
    }
}

@Preview
@Composable
private fun NightSmallFontPreview() {
    FuttyTheme(NightColorScheme) {
        FontPreviewSmall()
    }
}

@Composable
private fun FontPreviewBig() {
    Column {
        Font("displayLarge", typography.displayLarge)
        Font("displayMedium", typography.displayMedium)
        Font("displaySmall", typography.displaySmall)
        Font("headlineLarge", typography.headlineLarge)
        Font("headlineMedium", typography.headlineMedium)
        Font("headlineSmall", typography.headlineSmall)
    }
}

@Composable
private fun FontPreviewSmall() {
    Column {
        Font("titleLarge", typography.titleLarge)
        Font("titleMedium", typography.titleMedium)
        Font("titleSmall", typography.titleSmall)
        Font("bodyLarge", typography.bodyLarge)
        Font("bodyMedium", typography.bodyMedium)
        Font("bodySmall", typography.bodySmall)
        Font("labelLarge", typography.labelLarge)
        Font("labelMedium", typography.labelMedium)
        Font("labelSmall", typography.labelSmall)
    }
}

@Composable
fun Font(name: String, textStyle: TextStyle) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorGrey0())
            .padding(vertical = 12.dp, horizontal = 16.dp),
    ) {
        Text(
            text = name,
            style = textStyle,
            color = colorGrey900()
        )
    }
}
