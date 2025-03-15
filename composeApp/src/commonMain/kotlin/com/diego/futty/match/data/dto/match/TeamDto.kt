package com.diego.futty.match.data.dto.match

import com.diego.futty.match.domain.model.match.Team
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    @SerialName("id")
    val id: Int,

    @SerialName("score")
    val score: Int,

    @SerialName("name")
    val name: String,

    @SerialName("longName")
    val longName: String,
) {
    fun toModel() = Team(id, score, name, longName)
}
