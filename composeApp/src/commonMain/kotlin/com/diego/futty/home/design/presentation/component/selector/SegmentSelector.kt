package com.diego.futty.home.design.presentation.component.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900

@Suppress("ktlint:standard:function-naming")
@Composable
fun SegmentControl(
    modifier: Modifier = Modifier,
    segmentOptions: List<String>,
    selectedIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {},
) {
    var selectedItem by remember { mutableStateOf(selectedIndex) }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = colorGrey100(),
                    shape = RoundedCornerShape(CornerSize(50)),
                ).padding(4.dp)
                .then(modifier),
    ) {
        segmentOptions.forEachIndexed { index, text ->
            SegmentItem(
                modifier = Modifier.weight(1f),
                id = index,
                text = text,
                selected = index == selectedItem,
                onItemSelected = { newSelectedItem ->
                    selectedItem = newSelectedItem
                    onItemSelected(newSelectedItem)
                },
            )

            if (index < segmentOptions.size - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun SegmentItem(
    modifier: Modifier = Modifier,
    id: Int = 0,
    text: String = "Option",
    selected: Boolean,
    onItemSelected: (Int) -> Unit = {},
) {
    val backgroundColor = if (selected) colorGrey0() else colorGrey100()

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(CornerSize(50)))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(CornerSize(50)),
            ).clickable { onItemSelected(id) }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            ),
        text = text,
        style = typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = colorGrey900(),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
