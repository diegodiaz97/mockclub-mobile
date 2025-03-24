package com.diego.futty.home.match.data.dto.league

import com.diego.futty.home.match.domain.model.league.League
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeagueDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("localizedName")
    val localizedName: String,

    @SerialName("logo")
    val logo: String,
) {
    fun toModel() = League(id, name, localizedName, logo)
}
