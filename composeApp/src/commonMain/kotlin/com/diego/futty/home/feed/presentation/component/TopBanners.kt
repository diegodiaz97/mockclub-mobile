package com.diego.futty.home.feed.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.home.design.presentation.component.banner.BannerType
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.banner.ScrollBanner
import com.svenjacobs.reveal.RevealShape
import com.svenjacobs.reveal.RevealState
import com.svenjacobs.reveal.revealable
import compose.icons.TablerIcons
import compose.icons.tablericons.Focus
import compose.icons.tablericons.Shirt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBanners(
    scope: CoroutineScope,
    revealState: RevealState
) {
    ScrollBanner(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .revealable(
                key = Keys.Explore,
                state = revealState,
                shape = RevealShape.RoundRect(16.dp),
                onClick = { scope.launch { revealState.tryShowReveal(Keys.Post) } }
            ),
        bannerType = BannerType.Display,
        items = listOf(
            BannerUIData(
                title = "Explorar",
                color = colorInfoLight(),
                description = "Usa la sección de Explorar para inspirarte.",
                icon = TablerIcons.Focus,
            ),
            BannerUIData(
                title = "Crear",
                color = colorInfoLight(),
                description = "¿Ya creaste tu primer mockup? Únete a miles de diseñadores.",
                icon = TablerIcons.Shirt,
            ),
        )
    )
}
