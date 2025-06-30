package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.flowrow.MultipleFlowList
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel

@Composable
fun DesiresList(viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Intereses",
            style = typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorGrey900()
        )

        val chips = viewModel.chipItems.value
        if (chips != null) {
            MultipleFlowList(chips, viewModel.selectedChips.value) { index ->
                viewModel.onChipSelected(index)
            }
        } else {
            // SHIMMERS
        }
    }
}
