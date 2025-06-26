package com.diego.futty.design.presentation.preview

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import compose.icons.TablerIcons
import compose.icons.tablericons.Download

@Preview
@Composable
private fun ActionBannerPreview() {
    FuttyTheme {
        Banner.DisplayBanner(
            BannerUIData(
                icon = TablerIcons.Download,
                title = "Banner Accionable",
                description = "Éste es un banner que se puede accionar.",
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()
    }
}

@Preview
@Composable
private fun ActionBannerWithImagePreview() {
    FuttyTheme {
        Banner.DisplayBanner(
            BannerUIData(
                illustration = "",
                title = "Banner Accionable",
                description = "Éste es un banner que se puede accionar.",
                action = { }
            ),
            page = 0,
            state = rememberPagerState { 1 },
        ).Draw()
    }
}

@Preview
@Composable
private fun BorderBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            BannerUIData(
                title = "Dinero en cuenta:",
                description = "$ 254.300,59",
                status = BannerStatus.Border
            )
        ).Draw()
    }
}

@Preview
@Composable
private fun SuccessBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            BannerUIData(
                title = "¡Listo!",
                description = "Éste banner te avisará de situaciones positivas.",
                status = BannerStatus.Success
            )
        ).Draw()
    }
}

@Preview
@Composable
private fun ErrorBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            BannerUIData(
                title = "Algo salió mal",
                description = "Éste banner te avisará cuando algo salió mal.",
                status = BannerStatus.Error
            )
        ).Draw()
    }
}

@Preview
@Composable
private fun AlertBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            BannerUIData(
                title = "¡Cuidado!",
                description = "Éste banner te avisará cuando algo pueda salir mal.",
                status = BannerStatus.Alert
            )
        ).Draw()
    }
}

@Preview
@Composable
private fun InfoBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            BannerUIData(
                title = "¿Sabías?",
                description = "Éste banner te puede mostrar información.",
                status = BannerStatus.Info
            )
        ).Draw()
    }
}
