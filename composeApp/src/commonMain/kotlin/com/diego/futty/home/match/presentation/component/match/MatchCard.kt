package com.diego.futty.home.match.presentation.component.match

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey100
import com.diego.futty.core.presentation.theme.Grey300
import com.diego.futty.core.presentation.theme.Grey900

sealed class MatchCard {
    class HalfTime(
        private val time: String,
        private val home: String,
        private val away: String,
        private val score: String,
    ) : MatchCard() {
        @Composable
        override fun Draw() {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = time,
                    textAlign = TextAlign.Center,
                    style = typography.titleMedium,
                    color = Grey900
                )
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = Grey300)
                        .clickable { }
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = home,
                            textAlign = TextAlign.Center,
                            style = typography.titleMedium,
                            color = Grey100
                        )
                        Text(
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                            text = score,
                            textAlign = TextAlign.Center,
                            style = typography.headlineLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Grey0
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = away,
                            textAlign = TextAlign.Center,
                            style = typography.titleMedium,
                            color = Grey100
                        )
                    }
                }
            }
        }
    }

    @Composable
    abstract fun Draw()
}