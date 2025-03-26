package com.diego.futty.home.match.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.match.domain.model.league.League
import com.diego.futty.home.match.domain.model.match.LiveMatch

interface MatchViewContract {
    val buttonEnabled: State<Boolean>
    val liveScores: State<List<LiveMatch>?>
    val error: State<String>
    val leagues: State<List<League>?>
    val todayScores: State<List<LiveMatch>?>
    fun onButtonClicked()
}
