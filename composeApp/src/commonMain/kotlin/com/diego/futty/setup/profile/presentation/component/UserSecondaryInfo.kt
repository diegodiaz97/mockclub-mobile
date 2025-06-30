package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.home.design.presentation.component.card.SimpleInfoCard
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel

@Composable
fun UserSecondaryInfo(viewModel: ProfileViewModel) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleInfoCard(
            modifier = Modifier.weight(1f),
            title = "Publicaciones",
            description = viewModel.postsCant.value?.toString() ?: "",
        )
        SimpleInfoCard(
            modifier = Modifier.weight(1f),
            title = "Siguiendo",
            description = viewModel.followsCant.value?.toString() ?: "",
        )
        SimpleInfoCard(
            modifier = Modifier.weight(1f),
            title = "Seguidores",
            description = viewModel.followersCant.value?.toString() ?: "",
        )
    }
}
