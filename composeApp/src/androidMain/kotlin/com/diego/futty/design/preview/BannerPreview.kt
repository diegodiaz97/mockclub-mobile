package com.diego.futty.design.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.FuttyTheme
import com.diego.futty.design.presentation.component.banner.Banner
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
        Banner.BorderBanner(
            title = "Dinero en cuenta:",
            subtitle = "$ 254.300,59",
        ).Draw()
    }
}

@Preview
@Composable
private fun SuccessBannerPreview() {
    FuttyTheme {
        Banner.SuccessBanner(
            title = "¡Listo!",
            subtitle = "Éste banner te avisará de situaciones positivas.",
        ).Draw()
    }
}

@Preview
@Composable
private fun ErrorBannerPreview() {
    FuttyTheme {
        Banner.ErrorBanner(
            title = "Algo salió mal",
            subtitle = "Éste banner te avisará cuando algo salió mal.",
        ).Draw()
    }
}

@Preview
@Composable
private fun AlertBannerPreview() {
    FuttyTheme {
        Banner.AlertBanner(
            title = "¡Cuidado!",
            subtitle = "Éste banner te avisará cuando algo pueda salir mal.",
        ).Draw()
    }
}

@Preview
@Composable
private fun InfoBannerPreview() {
    FuttyTheme {
        Banner.InfoBanner(
            title = "¿Sabías?",
            subtitle = "Éste banner te puede mostrar información.",
        ).Draw()
    }
}