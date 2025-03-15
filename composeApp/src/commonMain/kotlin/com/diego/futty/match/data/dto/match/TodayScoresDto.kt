package com.diego.futty.match.data.dto.match

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodayScoresDto(
    @SerialName("matches")
    val liveScores: List<LiveMatchDto>? = emptyList()
)

@Serializable
data class ResponseTodayScoresDto(
    @SerialName("response")
    val response: TodayScoresDto? = null
)