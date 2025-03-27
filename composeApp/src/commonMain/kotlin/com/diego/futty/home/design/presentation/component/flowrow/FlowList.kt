package com.diego.futty.home.design.presentation.component.flowrow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.home.design.presentation.component.Chip.Chip
import com.diego.futty.home.design.presentation.component.Chip.ChipModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowList(list: List<ChipModel>, selectedIndex: Int, onSelected: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 5,
    ) {
        list.withIndex().forEach { item ->
            Chip(
                item.value.icon,
                item.value.text,
                item.value.color,
                list[selectedIndex] == item.value,
            ) {
                onSelected(item.index)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultipleFlowList(
    list: List<ChipModel>,
    selectedChips: List<ChipModel>,
    onSelected: (ChipModel) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 5,
    ) {
        list.withIndex().forEach { item ->
            Chip(
                item.value.icon,
                item.value.text,
                item.value.color,
                selectedChips.contains(item.value),
            ) {
                onSelected(item.value)
            }
        }
    }
}
