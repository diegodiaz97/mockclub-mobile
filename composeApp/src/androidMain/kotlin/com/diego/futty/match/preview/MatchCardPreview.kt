package com.diego.futty.match.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.match.presentation.component.match.MatchCard

@Preview
@Composable
private fun TopBarIcon() {
    FuttyTheme {
        MatchCard.HalfTime(
            time = "12:45",
            home = "FC Barcelona",
            away = "Real Madrid",
            score = "3 - 2",
        ).Draw()
    }
}
