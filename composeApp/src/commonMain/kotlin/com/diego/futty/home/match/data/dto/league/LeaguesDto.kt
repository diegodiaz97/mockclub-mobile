package com.diego.futty.home.match.data.dto.league

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaguesDto(
    @SerialName("leagues")
    val leagues: List<LeagueDto>? = emptyList()
)

@Serializable
data class ResponseLeagueDto(
    @SerialName("response")
    val response: LeaguesDto? = null
)
