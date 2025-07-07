package com.diego.futty.home.design.presentation.component.flowrow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.home.design.presentation.component.chip.Chip
import com.diego.futty.home.design.presentation.component.chip.ChipModel

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
                icon = item.value.icon,
                text = item.value.text,
                color = item.value.color,
                isSelected = list[selectedIndex] == item.value,
            ) {
                onSelected(item.index)
            }
        }
    }
}

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
                icon = item.value.icon,
                text = item.value.text,
                color = item.value.color,
                isSelected = selectedChips.contains(item.value),
            ) {
                onSelected(item.value)
            }
        }
    }
}
