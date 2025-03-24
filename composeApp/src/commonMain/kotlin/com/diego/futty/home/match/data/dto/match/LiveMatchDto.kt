package com.diego.futty.home.match.data.dto.match

import com.diego.futty.home.match.domain.model.match.LiveMatch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveMatchDto(
    @SerialName("id")
    val id: Int,

    @SerialName("leagueId")
    val leagueId: Int,

    @SerialName("time")
    val time: String,

    @SerialName("home")
    val home: TeamDto,

    @SerialName("away")
    val away: TeamDto,

    @SerialName("eliminatedTeamId")
    val eliminatedTeamId: Int?,

    @SerialName("statusId")
    val statusId: Int,

    @SerialName("tournamentStage")
    val tournamentStage: String,

    @SerialName("status")
    val status: LiveMatchStatusDto,

    @SerialName("timeTS")
    val timeTS: Long,
) {
    fun toModel() = LiveMatch(
        id,
        leagueId,
        time,
        home.toModel(),
        away.toModel(),
        eliminatedTeamId,
        statusId,
        tournamentStage,
        status.toModel(),
        timeTS
    )
}
