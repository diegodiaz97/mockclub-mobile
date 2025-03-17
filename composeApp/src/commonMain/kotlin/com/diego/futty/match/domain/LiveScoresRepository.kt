package com.diego.futty.match.domain

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.match.domain.model.league.League
import com.diego.futty.match.domain.model.match.LiveMatch

interface LiveScoresRepository {
    suspend fun getLiveScores(): DataResult<List<LiveMatch>?, DataError.Remote>
    suspend fun getLeagues(): DataResult<List<League>?, DataError.Remote>
    suspend fun getTodayScores(): DataResult<List<LiveMatch>?, DataError.Remote>
}