package com.diego.futty.match.data.dto.match

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveScoresDto(
    @SerialName("live")
    val liveScores: List<LiveMatchDto>? = emptyList()
)

@Serializable
data class ResponseLiveScoresDto(
    @SerialName("response")
    val response: LiveScoresDto? = null
)