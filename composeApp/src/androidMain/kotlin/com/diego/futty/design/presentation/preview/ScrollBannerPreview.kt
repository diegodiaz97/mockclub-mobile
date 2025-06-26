package com.diego.futty.design.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.colorAlert
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorInfo
import com.diego.futty.core.presentation.theme.colorSuccess
import com.diego.futty.home.design.presentation.component.banner.BannerType
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.banner.ScrollBanner

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BannerPreview() {
    ScrollBanner(
        bannerType = BannerType.FullImage,
        items =
            listOf(
                getBannerUIData().copy(color = colorInfo()),
                getBannerUIData().copy(illustration = "Res.drawable.compose_multiplatform"),
                getBannerUIData(),
                getBannerUIData().copy(color = colorError()),
                getBannerUIData().copy(color = colorAlert()),
                getBannerUIData().copy(color = colorSuccess()),
            ),
    )
}

private fun getBannerUIData() = BannerUIData(
    title = "Title",
    description = "Unica tarjeta de crédito con el 99% de aprobación. Máximo 2 líneas",
    label = "Tres palabras máximo",
)
