package com.diego.futty.match.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.Result
import com.diego.futty.match.data.dto.league.ResponseLeagueDto
import com.diego.futty.match.data.dto.match.ResponseLiveScoresDto
import com.diego.futty.match.data.dto.match.ResponseTodayScoresDto

interface RemoteLiveScoresDataSource {
    suspend fun getLeagues(): Result<ResponseLeagueDto, DataError.Remote>
    suspend fun getLiveScores(): Result<ResponseLiveScoresDto, DataError.Remote>
    suspend fun getTodayScores(date: String): Result<ResponseTodayScoresDto, DataError.Remote>
}
