package com.diego.futty.home.design.presentation.component.bottomsheet

data class OptionItem(
    val text: String,
    val action: () -> Unit,
    val type: OptionType = OptionType.Basic,
)

enum class OptionType {
    Basic,
    Error,
}
