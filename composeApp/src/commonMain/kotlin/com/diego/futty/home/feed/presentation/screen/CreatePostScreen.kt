package com.diego.futty.home.feed.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorSuccessLight
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressBar
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressSize
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel

@Composable
fun CreatePostScreen(viewModel: FeedViewModel) {
    if (viewModel.showPostCreation.value) {
        BottomSheet(
            draggable = true,
            containerColor = colorGrey0(),
            onDismiss = { viewModel.dismissPostCreation() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TopBar(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp),
                    title = "Nuevo post",
                    topBarActionType = TopBarActionType.Button(
                        text = "Postear",
                        onClick = { viewModel.createPost() }
                    ),
                    onBack = { viewModel.dismissPostCreation() }
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Avatar.ProfileAvatar(
                        imageUrl = viewModel.user.value?.profileImage?.image,
                        initials = viewModel.user.value?.profileImage?.initials,
                        background = viewModel.user.value?.profileImage?.background?.toColor(),
                        avatarSize = AvatarSize.Big,
                        onClick = { }
                    ).Draw()

                    TextInput.FullScreenInput(
                        input = viewModel.text.value,
                        placeholder = "¿Que has diseñado?",
                        onFocusChanged = { /*viewModel.hideKeyboard()*/ },
                        onTextChangeAction = { viewModel.updateText(it) }
                    ).Draw()
                }

                HorizontalDivider(thickness = 1.dp, color = colorGrey100())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextInput.Input(
                        modifier = Modifier.weight(1f),
                        input = viewModel.team.value,
                        label = "Equipo",
                        placeholder = viewModel.team.value,
                        onFocusChanged = { /*viewModel.hideKeyboard()*/ },
                        onTextChangeAction = { viewModel.updateTeam(it) }
                    ).Draw()

                    TextInput.Input(
                        modifier = Modifier.weight(1f),
                        input = viewModel.brand.value,
                        label = "Marca",
                        placeholder = viewModel.brand.value,
                        onFocusChanged = { /*viewModel.hideKeyboard()*/ },
                        onTextChangeAction = { viewModel.updateBrand(it) }
                    ).Draw()

                    val difference = viewModel.postMaxLength.value.toFloat() - viewModel.text.value.length.toFloat()
                    val progress = viewModel.text.value.length.toFloat() / viewModel.postMaxLength.value.toFloat()

                    CircularProgressBar(
                        progress = progress,
                        size = CircularProgressSize.Small,
                        text = difference.toInt().toString(),
                        color = when {
                            difference < 11 -> colorErrorLight()
                            difference < 21 -> colorAlertLight()
                            else -> colorSuccessLight()
                        }
                    )
                }
            }
        }
    }
}
