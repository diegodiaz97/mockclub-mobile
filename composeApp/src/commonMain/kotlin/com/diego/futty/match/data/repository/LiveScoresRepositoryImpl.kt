package com.diego.futty.match.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.Result
import com.diego.futty.core.domain.map
import com.diego.futty.match.data.network.RemoteLiveScoresDataSource
import com.diego.futty.match.domain.LiveScoresRepository
import com.diego.futty.match.domain.model.league.League
import com.diego.futty.match.domain.model.match.LiveMatch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class LiveScoresRepositoryImpl(
    private val remoteLiveScoresDataSource: RemoteLiveScoresDataSource,
) : LiveScoresRepository {
    override suspend fun getLeagues(): Result<List<League>?, DataError.Remote> {
        return remoteLiveScoresDataSource
            .getLeagues()
            .map { dto ->
                dto.response?.leagues?.map { it.toModel() }
            }
    }

    override suspend fun getLiveScores(): Result<List<LiveMatch>?, DataError.Remote> {
        return remoteLiveScoresDataSource
            .getLiveScores()
            .map { dto ->
                dto.response?.liveScores?.map { it.toModel() }
            }
    }

    override suspend fun getTodayScores(): Result<List<LiveMatch>?, DataError.Remote> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            .replace("-", "")

        return remoteLiveScoresDataSource
            .getTodayScores(today)
            .map { dto ->
                dto.response?.liveScores?.map { it.toModel() }
            }
    }
}