package com.diego.futty.design.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import org.jetbrains.compose.resources.painterResource

@Preview
@Composable
private fun ActionBannerPreview() {
    FuttyTheme {
        Banner.ClickableBanner(
            image = null,
            title = "Banner Accionable",
            subtitle = "Éste es un banner que se puede accionar.",
            onClick = { }
        ).Draw()
    }
}

@Preview
@Composable
private fun ActionBannerWithImagePreview() {
    FuttyTheme {
        Banner.ClickableBanner(
            image = painterResource(Res.drawable.book_error_2),
            title = "Banner Accionable",
            subtitle = "Éste es un banner que se puede accionar.",
            onClick = { }
        ).Draw()
    }
}

@Preview
@Composable
private fun BorderBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            title = "Dinero en cuenta:",
            subtitle = "$ 254.300,59",
            status = BannerStatus.Border
        ).Draw()
    }
}

@Preview
@Composable
private fun SuccessBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            title = "¡Listo!",
            subtitle = "Éste banner te avisará de situaciones positivas.",
            status = BannerStatus.Success
        ).Draw()
    }
}

@Preview
@Composable
private fun ErrorBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            title = "Algo salió mal",
            subtitle = "Éste banner te avisará cuando algo salió mal.",
            status = BannerStatus.Error
        ).Draw()
    }
}

@Preview
@Composable
private fun AlertBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            title = "¡Cuidado!",
            subtitle = "Éste banner te avisará cuando algo pueda salir mal.",
            status = BannerStatus.Alert
        ).Draw()
    }
}

@Preview
@Composable
private fun InfoBannerPreview() {
    FuttyTheme {
        Banner.StatusBanner(
            title = "¿Sabías?",
            subtitle = "Éste banner te puede mostrar información.",
            status = BannerStatus.Info
        ).Draw()
    }
}