package com.diego.futty.home.match.domain.model.match

data class LiveMatch(
    val id: Int,
    val leagueId: Int,
    val time: String,
    val home: Team,
    val away: Team,
    val eliminatedTeamId: Int?,
    val statusId: Int,
    val tournamentStage: String,
    val status: LiveMatchStatus,
    val timeTS: Long,
)
