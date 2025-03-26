package com.diego.futty.setup.profile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorInfoLight
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Settings
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.girasoles
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
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val image = painterResource(Res.drawable.girasoles)
        val initials = "DD"

        val profileImage = if (image == null) {
            Avatar.FullImageAvatar(
                image = image,
                avatarSize = AvatarSize.Extra,
                onClick = { /* Update profile image */ }
            )
        } else {
            Avatar.InitialsAvatar(
                initials = initials,
                tint = colorGrey900(),
                background = colorInfoLight(),
                avatarSize = AvatarSize.Extra,
                onClick = { /* Update profile image */ }
            )
        }

        MainInfo(profileImage, "Diego Díaz", "Tandil, Argentina")

        /*BlurredImage(
            image = painterResource(Res.drawable.girasoles),
            blur = 50.dp
        ) {

        }*/
    }
}

@Composable
private fun MainInfo(
    profileImage: Avatar,
    name: String,
    description: String,
) {
    Row(
        modifier = Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        profileImage.Draw()
        Column(verticalArrangement = Arrangement.SpaceAround) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorGrey900()
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = colorGrey500()
            )
        }
    }
}
