package com.diego.futty.match.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.match.domain.model.league.League
import com.diego.futty.match.domain.model.match.LiveMatch

interface MatchViewContract {
    val buttonEnabled: State<Boolean>
    val liveScores: State<List<LiveMatch>?>
    val error: State<String>
    val leagues: State<List<League>?>

    fun onButtonPressed()
    val todayScores: State<List<LiveMatch>?>
}
