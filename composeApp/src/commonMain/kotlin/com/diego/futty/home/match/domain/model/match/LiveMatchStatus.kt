package com.diego.futty.home.match.domain.model.match

data class LiveMatchStatus(
    val utcTime: String,
    val started: Boolean,
    val finished: Boolean,
    val cancelled: Boolean,
    val ongoing: Boolean,
    val scoreStr: String,
    val liveTime: LiveTime?,
    val aggregatedStr: String,
)
