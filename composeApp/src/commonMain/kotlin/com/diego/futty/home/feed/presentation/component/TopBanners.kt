package com.diego.futty.home.feed.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.home.design.presentation.component.banner.BannerType
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.banner.ScrollBanner
import com.svenjacobs.reveal.RevealShape
import com.svenjacobs.reveal.RevealState
import com.svenjacobs.reveal.revealable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBanners(
    scope: CoroutineScope,
    revealState: RevealState,
    action: () -> Unit,
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
        bannerType = BannerType.FullImage,
        items = listOf(
            BannerUIData(
                title = "Nuevo Challenge",
                description = "¡Diseña, compite y gana!",
                illustration = "https://img.freepik.com/premium-photo/colorful-background-with-pink-blue-waves_68067-1914.jpg",
                label = "Participar",
                action = { action() },
            ),
        )
    )
}
