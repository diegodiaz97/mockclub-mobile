package com.diego.futty.design.presentation.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.colorAlert
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfo
import com.diego.futty.core.presentation.theme.colorSuccess
import com.diego.futty.design.presentation.component.avatar.Avatar
import com.diego.futty.design.presentation.component.avatar.AvatarSize
import com.diego.futty.design.presentation.component.button.PrimaryButton
import com.diego.futty.design.presentation.component.button.SecondaryButton
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, onAction: () -> Unit) {
    FlexibleBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 0.9f,
                intermediatelyExpanded = 0.5f,
                slightlyExpanded = 0.3f,
            ),
            isModal = true,
            skipSlightlyExpanded = false,
        ),
        containerColor = colorGrey100(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "This is Flexible Bottom Sheet",
                style = typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey900(),
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 5,
            ) {
                initials.forEach { initial ->
                    Avatar.InitialsAvatar(
                        initials = initial,
                        tint = Grey0,
                        background = getRandomColor(),
                        avatarSize = AvatarSize.Big
                    ).Draw()
                }
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Aceptar",
                onClick = { onAction() }
            )
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Cancelar",
                onClick = { onAction() }
            )
        }
    }
}


private val initials = listOf("CM", "DC", "JC", "PD", "AT", "CF", "DD", "CH", "AP")

@Composable
private fun getRandomColor(): Color {
    val colors = listOf(colorSuccess(), colorError(), colorAlert(), colorInfo())
    val index = Random.nextInt(4)

    return colors[index]
}