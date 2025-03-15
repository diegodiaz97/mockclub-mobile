package com.diego.futty.match.domain.model.match

data class LiveTime(
    val short: String,
    val shortKey: String,
    val long: String,
    val longKey: String,
    val maxTime: Int,
    val addedTime: Int,
)
