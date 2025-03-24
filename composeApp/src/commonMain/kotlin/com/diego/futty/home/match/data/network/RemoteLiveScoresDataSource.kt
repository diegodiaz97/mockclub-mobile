package com.diego.futty.home.match.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.match.data.dto.league.ResponseLeagueDto
import com.diego.futty.home.match.data.dto.match.ResponseLiveScoresDto
import com.diego.futty.home.match.data.dto.match.ResponseTodayScoresDto

interface RemoteLiveScoresDataSource {
    suspend fun getLeagues(): DataResult<ResponseLeagueDto, DataError.Remote>
    suspend fun getLiveScores(): DataResult<ResponseLiveScoresDto, DataError.Remote>
    suspend fun getTodayScores(date: String): DataResult<ResponseTodayScoresDto, DataError.Remote>
}
