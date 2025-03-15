package com.diego.futty.design.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.design.presentation.component.banner.BannerUIData
import com.diego.futty.design.presentation.component.banner.ScrollBanner
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import futty.composeapp.generated.resources.compose_multiplatform
import futty.composeapp.generated.resources.girasoles

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BannerPreview() {
    ScrollBanner(
        items =
        listOf(
            getBannerUIData().copy(style = BannerUIData.BannerStyle.Primary, illustration = Res.drawable.girasoles),
            getBannerUIData().copy(style = BannerUIData.BannerStyle.Dark, illustration = Res.drawable.compose_multiplatform),
            getBannerUIData(),
            getBannerUIData().copy(style = BannerUIData.BannerStyle.Yellow),
            getBannerUIData().copy(style = BannerUIData.BannerStyle.Pink),
            getBannerUIData().copy(style = BannerUIData.BannerStyle.Green),
        ),
    )
}

private fun getBannerUIData() = BannerUIData(
    title = "Title",
    description = "Unica tarjeta de crédito con el 99% de aprobación. Máximo 2 líneas",
    labelAction = "Tres palabras máximo",
)
