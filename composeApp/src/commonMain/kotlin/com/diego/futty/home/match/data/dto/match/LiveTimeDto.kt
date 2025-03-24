package com.diego.futty.home.match.data.dto.match

import com.diego.futty.home.match.domain.model.match.LiveTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveTimeDto(
    @SerialName("short")
    val short: String,

    @SerialName("shortKey")
    val shortKey: String,

    @SerialName("long")
    val long: String,

    @SerialName("longKey")
    val longKey: String,

    @SerialName("maxTime")
    val maxTime: Int,

    @SerialName("addedTime")
    val addedTime: Int,
) {
    fun toModel() = LiveTime(short, shortKey, long, longKey, maxTime, addedTime)
}
