package com.diego.futty.home.match.data.network

import com.diego.futty.core.data.remote.safeCall
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.match.data.dto.league.ResponseLeagueDto
import com.diego.futty.home.match.data.dto.match.ResponseLiveScoresDto
import com.diego.futty.home.match.data.dto.match.ResponseTodayScoresDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

private const val BASE_URL = "https://free-api-live-football-data.p.rapidapi.com"

class KtorRemoteLiveScoresDataSource(
    private val httpClient: HttpClient
) : RemoteLiveScoresDataSource {

    override suspend fun getLiveScores(): DataResult<ResponseLiveScoresDto, DataError.Remote> {
        return safeCall<ResponseLiveScoresDto> {
            httpClient.get(
                urlString = "$BASE_URL/football-current-live"
            ) {
                header("x-rapidapi-key", "2a369f543fmsh6e8d32020f74d6ep19d8d7jsn924387c49480")
                header("x-rapidapi-host", "free-api-live-football-data.p.rapidapi.com")
            }
        }
    }

    override suspend fun getTodayScores(date: String): DataResult<ResponseTodayScoresDto, DataError.Remote> {
        return safeCall<ResponseTodayScoresDto> {
            httpClient.get(
                urlString = "$BASE_URL/football-get-matches-by-date"
            ) {
                header("x-rapidapi-key", "2a369f543fmsh6e8d32020f74d6ep19d8d7jsn924387c49480")
                header("x-rapidapi-host", "free-api-live-football-data.p.rapidapi.com")
                parameter("date", date)
            }
        }
    }

    override suspend fun getLeagues(): DataResult<ResponseLeagueDto, DataError.Remote> {
        return safeCall<ResponseLeagueDto> {
            httpClient.get(
                urlString = "$BASE_URL/football-get-all-leagues"
            ) {
                header("x-rapidapi-key", "2a369f543fmsh6e8d32020f74d6ep19d8d7jsn924387c49480")
                header("x-rapidapi-host", "free-api-live-football-data.p.rapidapi.com")
            }
        }
    }
}
