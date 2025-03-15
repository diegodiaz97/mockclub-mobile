package com.diego.futty.match.presentation.component.league

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.diego.futty.core.presentation.Grey100
import com.diego.futty.core.presentation.Grey900
import com.diego.futty.core.presentation.PulseAnimation
import com.diego.futty.match.domain.model.league.League
import com.diego.futty.match.domain.model.match.LiveMatch
import com.diego.futty.match.presentation.component.match.MatchCard
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import org.jetbrains.compose.resources.painterResource

@Composable
fun LeagueCard(
    leagues: List<League>,
    liveScores: List<LiveMatch>,
    paddingValues: PaddingValues
) {
    val grouped = liveScores.groupBy { it.leagueId }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        grouped.forEach { (league, matches) ->
            item {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Grey100)
                        .clickable { }
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val logo = try {
                            leagues.first { league == it.id }.logo
                        } catch (e: Exception) {
                            null
                        }
                        //LeagueLogo(logo)
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = league.toString(),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Grey900
                        )
                    }
                    matches.forEach { match ->
                        MatchCard.HalfTime(
                            time = match.status.liveTime?.long ?: "",
                            home = match.home.name,
                            away = match.away.name,
                            score = match.status.scoreStr,
                        ).Draw()
                    }
                }
            }
        }
    }
}

@Composable
private fun LeagueLogo(logoUrl: String?) {
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(null)
    }
    val painter = rememberAsyncImagePainter(
        model = logoUrl,
        onSuccess = {
            imageLoadResult =
                if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                    Result.success(it.painter)
                } else {
                    Result.failure(Exception("Invalid image size"))
                }
        },
        onError = {
            it.result.throwable.printStackTrace()
            imageLoadResult = Result.failure(it.result.throwable)
        }
    )

    when (val result = imageLoadResult) {
        null -> PulseAnimation(
            modifier = Modifier.size(60.dp)
        )

        else -> {
            Image(
                painter = if (result.isSuccess) {
                    painter
                } else {
                    painterResource(Res.drawable.book_error_2)
                },
                contentDescription = "",
                contentScale = if (result.isSuccess) {
                    ContentScale.Crop
                } else {
                    ContentScale.Fit
                },
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
