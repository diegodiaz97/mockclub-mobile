package com.diego.futty.match.data.dto.match

import com.diego.futty.match.domain.model.match.LiveMatchStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveMatchStatusDto(
    @SerialName("utcTime")
    val utcTime: String,

    @SerialName("started")
    val started: Boolean,

    @SerialName("finished")
    val finished: Boolean,

    @SerialName("cancelled")
    val cancelled: Boolean,

    @SerialName("ongoing")
    val ongoing: Boolean? = null,

    @SerialName("scoreStr")
    val scoreStr: String? = null,

    @SerialName("liveTime")
    val liveTime: LiveTimeDto? = null,

    @SerialName("aggregatedStr")
    val aggregatedStr: String? = null,
) {
    fun toModel() = LiveMatchStatus(
        utcTime,
        started,
        finished,
        cancelled,
        ongoing ?: false,
        scoreStr ?: "-",
        liveTime?.toModel(),
        aggregatedStr ?: ""
    )
}
