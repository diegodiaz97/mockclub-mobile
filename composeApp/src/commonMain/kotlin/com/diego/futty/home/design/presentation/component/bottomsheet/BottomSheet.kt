package com.diego.futty.home.design.presentation.component.bottomsheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey100
import com.skydoves.flexible.bottomsheet.material3.BottomSheetDefaults
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState

@Composable
fun FlexibleBottomSheet(
    isModal: Boolean = true,
    onDismiss: () -> Unit,
    containerColor: Color = colorGrey100(),
    content: @Composable () -> Unit,
) {
    FlexibleBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 0.9f,
                intermediatelyExpanded = 0.5f,
                slightlyExpanded = 0.3f,
            ),
            isModal = isModal,
            skipSlightlyExpanded = false,
        ),
        shape = RoundedCornerShape(36.dp),
        containerColor = containerColor,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colorError()) },
    ) { content() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    draggable: Boolean = false,
    onDismiss: () -> Unit,
    containerColor: Color = colorGrey100(),
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { draggable }
        ),
        shape = RoundedCornerShape(36.dp),
        containerColor = containerColor,
        dragHandle = null,
    ) {
        content()
    }
}
