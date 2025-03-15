package com.diego.futty.match.domain

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.Result
import com.diego.futty.match.domain.model.league.League
import com.diego.futty.match.domain.model.match.LiveMatch

interface LiveScoresRepository {
    suspend fun getLiveScores(): Result<List<LiveMatch>?, DataError.Remote>
    suspend fun getLeagues(): Result<List<League>?, DataError.Remote>
    suspend fun getTodayScores(): Result<List<LiveMatch>?, DataError.Remote>
}