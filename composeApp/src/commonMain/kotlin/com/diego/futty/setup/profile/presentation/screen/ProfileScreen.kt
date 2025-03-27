package com.diego.futty.setup.profile.presentation.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.flowrow.MultipleFlowList
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressBar
import com.diego.futty.home.design.presentation.component.progressbar.LinearProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Settings
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Perfil",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.Settings,
                    onClick = { viewModel.onSettingsClicked() }
                ),
                onBack = { viewModel.onBackClicked() }
            )
        },
        content = { paddingValues ->
            ProfileContent(viewModel, paddingValues)
        },
    )
}

@Composable
private fun ProfileContent(viewModel: ProfileViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MainInfo(viewModel)
        Desires(viewModel)
        Levels()
    }
}

@Composable
fun Levels() {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = "Niveles",
            style = typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorGrey900()
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.width(8.dp))
            CircularProgressBar(progress = 0.2f, text = "Nivel 1")
            CircularProgressBar(progress = 0.4f, text = "Nivel 2", color = colorErrorLight())
            CircularProgressBar(progress = 0.6f, text = "Nivel 3", color = colorAlertLight())
            CircularProgressBar(progress = 0.8f, text = "Nivel 4", color = colorInfoLight())
            CircularProgressBar(progress = 1.0f, text = "Nivel 5", color = colorGrey700())
            Spacer(Modifier.width(8.dp))
        }
        LinearProgressBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            progress = 0.4f,
            color = colorGrey900()
        )
    }
}

@Composable
private fun MainInfo(viewModel: ProfileViewModel) {
    val user = viewModel.user.value
    if (user != null) {
        Row(
            modifier = Modifier.padding(top = 12.dp).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image = user.profileImage.image
            Avatar.ProfileAvatar(
                modifier = Modifier.align(Alignment.Top),
                image = if (image != null) painterResource(image) else null,
                initials = user.profileImage.initials,
                background = user.profileImage.background?.toColor(),
                avatarSize = AvatarSize.Extra,
                onClick = { }
            ).Draw()
            Column(verticalArrangement = Arrangement.SpaceAround) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = user.name,
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorGrey900()
                )
                if (user.description != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = user.description,
                        style = typography.titleSmall,
                        fontWeight = FontWeight.Normal,
                        color = colorGrey400()
                    )
                }
            }
        }
    } else {
        // SHIMMERS
    }
}

@Composable
private fun Desires(viewModel: ProfileViewModel) {
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
