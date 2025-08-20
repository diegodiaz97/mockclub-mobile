package com.diego.futty.home.postCreation.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Plus
import com.adamglin.phosphoricons.bold.X
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorSecondary
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.chip.Chip
import com.diego.futty.home.design.presentation.component.input.TextInput

@Composable
fun TagsSelector(
    existingTags: List<String>,
    selectedTags: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit,
) = BottomSheet(
    draggable = true,
    onDismiss = { onDismiss() },
) {
    var tags by remember { mutableStateOf(selectedTags) }
    var newTag by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp),
            text = "Tags",
            style = typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )

        AnimatedVisibility(
            visible = existingTags.isNotEmpty()
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxLines = 3
            ) {
                existingTags.withIndex().forEach { item ->
                    Chip(
                        text = item.value,
                        color = colorGrey900(),
                        unselectedColor = colorGrey0(),
                        selectedTextColor = colorGrey0(),
                        isSelected = tags.contains(item.value),
                    ) {
                        if (tags.contains(item.value)) {
                            tags = tags.filterIndexed { _, value -> value != item.value }
                        } else {
                            if (tags.size < 5) {
                                tags = tags.plus(item.value)
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = tags.isNotEmpty()
        ) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item { Spacer(Modifier.width(12.dp)) }
                tags.forEachIndexed { index, tag ->
                    item {
                        Row {
                            Chip(
                                icon = PhosphorIcons.Bold.X,
                                text = tag,
                                color = colorSecondary(),
                                isSelected = true,
                            ) { tags = tags.filterIndexed { i, _ -> i != index } }
                            /*Avatar.IconAvatar(
                                modifier = Modifier.offset(x = (-14).dp, y = (-8).dp),
                                icon = PhosphorIcons.Bold.X,
                                tint = colorGrey600(),
                                background = colorGrey0(),
                                avatarSize = AvatarSize.Atomic,
                                onClick = {
                                    tags = tags.filterIndexed { i, _ -> i != index }
                                }
                            ).Draw()
                            Icon(
                                modifier = Modifier
                                    .offset(x = (-10).dp)
                                    .size(22.dp)
                                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                                    .background(colorGrey0())
                                    .padding(4.dp)
                                    .clickable { tags = tags.filterIndexed { i, _ -> i != index } },
                                imageVector = PhosphorIcons.Bold.X,
                                contentDescription = "close tag post",
                                tint = colorGrey600(),
                            )*/
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextInput.Input(
                modifier = Modifier.weight(1f),
                input = newTag,
                placeholder = "Agregar tag (máximo 12 caracteres)",
                background = colorGrey0(),
                onTextChangeAction = {
                    if (it.length <= 12) {
                        newTag = it.lowercase().trim()
                    }
                }
            ).Draw()
            Avatar.IconAvatar(
                icon = PhosphorIcons.Bold.Plus,
                background = colorGrey0(),
                tint = colorGrey400(),
                onClick = {
                    if (newTag.isNotEmpty() && tags.contains(newTag).not() && tags.size < 5) {
                        tags = tags.plus(newTag)
                        newTag = ""
                    }
                }
            ).Draw()
        }

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            title = "Listo",
            isEnabled = tags.isNotEmpty(),
            onClick = { onConfirm(tags) }
        )
    }
}
