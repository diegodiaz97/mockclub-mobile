package com.diego.futty.home.feed.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.colorSecondary
import com.svenjacobs.reveal.Key
import com.svenjacobs.reveal.RevealOverlayArrangement
import com.svenjacobs.reveal.RevealOverlayScope
import com.svenjacobs.reveal.RevealState
import com.svenjacobs.reveal.shapes.balloon.Arrow
import com.svenjacobs.reveal.shapes.balloon.Balloon


@Composable
fun RevealOverlayScope.RevealOverlayContent(key: Key) {
    when (key) {
        Keys.Profile -> OverlayText(
            modifier = Modifier.align(
                verticalArrangement = RevealOverlayArrangement.Bottom,
                horizontalAlignment = Alignment.End,
            ),
            text = "Visualiza y edita tu Perfil",
            arrow = Arrow.top(horizontalAlignment = Alignment.End),
        )

        Keys.Explore -> OverlayText(
            modifier = Modifier.align(
                verticalArrangement = RevealOverlayArrangement.Bottom,
            ),
            text = "No te pierdas las últimas novedades",
            arrow = Arrow.top(),
        )

        Keys.Post -> OverlayText(
            modifier = Modifier.align(
                horizontalArrangement = RevealOverlayArrangement.Start,
            ),
            text = "Postea tu primer Mockup",
            arrow = Arrow.end(),
        )
    }
}

@Composable
private fun OverlayText(
    modifier: Modifier = Modifier,
    text: String,
    arrow: Arrow,
) {
    Balloon(
        modifier = modifier.padding(8.dp),
        arrow = arrow,
        backgroundColor = colorSecondary(),
        elevation = 2.dp,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            color = Grey0,
            style = typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}


suspend fun showNextReveal(revealState: RevealState, key: Key) {
    when (key) {
        Keys.Profile -> revealState.tryShowReveal(Keys.Explore)

        Keys.Explore -> revealState.tryShowReveal(Keys.Post)

        Keys.Post -> try {
            revealState.hide()
        } catch (e: Exception) {
            // Ignore
        }
    }
}

suspend fun RevealState.tryShowReveal(key: Key) {
    try {
        if (containsRevealable(key)) {
            reveal(key)
        }
    } catch (e: IllegalArgumentException) {
        println("❌ Reveal failed for key: $key -> ${e.message}")
    }
}

enum class Keys { Post, Profile, Explore }
