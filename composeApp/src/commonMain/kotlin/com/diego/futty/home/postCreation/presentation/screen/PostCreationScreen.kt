package com.diego.futty.home.postCreation.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Plus
import com.adamglin.phosphoricons.bold.X
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.colorPrimary
import com.diego.futty.core.presentation.theme.colorSecondary
import com.diego.futty.core.presentation.theme.colorSuccessLight
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.chip.Chip
import com.diego.futty.home.design.presentation.component.image.AspectRatio
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.post.PostLogosSelection
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressSize
import com.diego.futty.home.design.presentation.component.progressbar.DynamicCircularProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.presentation.component.LogosSelector
import com.diego.futty.home.postCreation.presentation.component.TagsSelector
import com.diego.futty.home.postCreation.presentation.viewmodel.PostCreationViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Photo
import compose.icons.tablericons.Plus
import compose.icons.tablericons.RectangleVertical
import compose.icons.tablericons.Square
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostCreationScreen(
    viewModel: PostCreationViewModel,
    user: User?,
    onClose: () -> Unit,
    onStartPostCreation: () -> Unit,
    onPostCreated: () -> Unit,
) {
    BackHandler {
        viewModel.dismissPostCreation()
        onClose()
    }

    if (user == null) return
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Nuevo Post",
                topBarActionType = TopBarActionType.Button(
                    text = "Postear",
                    onClick = {
                        viewModel.createPost(
                            onStartPostCreation = { onStartPostCreation() },
                            onPostCreated = { onPostCreated() },
                        )
                    }
                ),
                onBack = {
                    onClose()
                    viewModel.dismissPostCreation()
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar.ProfileAvatar(
                        imageUrl = user.profileImage?.image,
                        initials = user.profileImage?.initials,
                        background = user.profileImage?.background?.toColor(),
                        avatarSize = AvatarSize.Big,
                        onClick = { }
                    ).Draw()
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "${user.name} ${user.lastName}",
                                style = typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = colorGrey900()
                            )
                            if (user.userType == USER_TYPE_PRO) {
                                VerifiedIcon(Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }

                TextInput.FullScreenInput(
                    input = viewModel.text.value,
                    placeholder = "¿Que has diseñado?",
                    onTextChangeAction = { viewModel.updateText(it) }
                ).Draw()
            }
        },
        bottomBar = {
            PostFooter(viewModel)
        }
    )

    if (viewModel.showIdentity.value) {
        LogosSelector(
            teamName = viewModel.team.value,
            brandName = viewModel.brand.value,
            teamLogo = viewModel.teamLogo.value,
            brandLogo = viewModel.brandLogo.value,
            designerLogo = viewModel.designerLogo.value,
            onDismiss = { viewModel.showIdentity() },
            onConfirm = { newIdentity -> viewModel.updateIdentity(newIdentity) }
        )
    }

    if (viewModel.showTags.value) {
        TagsSelector(
            existingTags = viewModel.existingTags.value,
            selectedTags = viewModel.newTags.value,
            onDismiss = { viewModel.showTags() },
            onConfirm = { tags -> viewModel.addTags(tags) },
        )
    }
}

fun Modifier.adaptiveBottomBarPadding(imeVisible: Boolean): Modifier {
    return if (imeVisible) {
        if (PlatformInfo.isIOS) this else this.padding(bottom = 12.dp)
    } else {
        this.navigationBarsPadding()
    }
}

@Composable
private fun PostFooter(viewModel: PostCreationViewModel) {
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .adaptiveBottomBarPadding(imeVisible),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val tags = viewModel.newTags.value

        AnimatedVisibility(
            visible = viewModel.images.value.isNotEmpty()
        ) {
            SelectedImages(viewModel)
        }

        AnimatedVisibility(
            visible = viewModel.teamLogo.value != null || viewModel.brandLogo.value != null
        ) {
            PostLogosSelection(
                modifier = Modifier.padding(horizontal = 16.dp),
                teamLogo = viewModel.teamLogo.value,
                brandLogo = viewModel.brandLogo.value,
                designerLogo = viewModel.designerLogo.value,
            )
        }

        AnimatedVisibility(
            visible = tags.isNotEmpty()
        ) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item { Spacer(Modifier.width(16.dp)) }
                tags.forEachIndexed { index, tag ->
                    item {
                        Row {
                            Chip(
                                text = tag,
                                icon = PhosphorIcons.Bold.X,
                                color = colorSecondary(),
                                isSelected = true,
                            ) { viewModel.removeTag(index) }
                        }
                    }
                }
                item { Spacer(Modifier.width(16.dp)) }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = colorGrey100())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Chip(
                icon = TablerIcons.Plus,
                text = "Tags",
                color = colorGrey100(),
                isSelected = false,
                onClick = { viewModel.showTags() }
            )

            Chip(
                icon = TablerIcons.Plus,
                text = "Identidad",
                color = colorGrey100(),
                isSelected = false,
                onClick = { viewModel.showIdentity() }
            )

            Spacer(modifier = Modifier.weight(1f).background(colorPrimary()))

            /*TextInput.Input(
                modifier = Modifier.weight(1f),
                input = viewModel.team.value,
                placeholder = "Equipo",
                onFocusChanged = { /*viewModel.hideKeyboard()*/ },
                onTextChangeAction = { viewModel.updateTeam(it) }
            ).Draw()

            TextInput.Input(
                modifier = Modifier.weight(1f),
                input = viewModel.brand.value,
                placeholder = "Marca",
                onFocusChanged = { /*viewModel.hideKeyboard()*/ },
                onTextChangeAction = { viewModel.updateBrand(it) }
            ).Draw()*/

            Chip(
                icon = if (viewModel.imageRatio.value == AspectRatio.Square) {
                    TablerIcons.Square
                } else {
                    TablerIcons.RectangleVertical
                },
                text = if (viewModel.imageRatio.value == AspectRatio.Square) {
                    "1:1"
                } else {
                    "4:5"
                },
                color = colorGrey100(),
                isSelected = false,
                onClick = { viewModel.updateRatio() }
            )

            GalleryView(viewModel)

            val difference =
                viewModel.postMaxLength.value.toFloat() - viewModel.text.value.length.toFloat()
            val progress =
                viewModel.text.value.length.toFloat() / viewModel.postMaxLength.value.toFloat()

            DynamicCircularProgressBar(
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

@Composable
fun GalleryView(viewModel: PostCreationViewModel) {
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Multiple(maxItems = 10),
    ) { images ->
        if (images == null) return@rememberFilePickerLauncher
        viewModel.onImagesSelected(images)
    }

    Avatar.IconAvatar(
        modifier = Modifier,
        icon = TablerIcons.Photo,
        tint = colorGrey900(),
        background = colorGrey0(),
        avatarSize = AvatarSize.Small,
        onClick = { launcher.launch() }
    ).Draw()
}

@Composable
fun SelectedImages(viewModel: PostCreationViewModel) = LazyRow(
    modifier = Modifier.fillMaxWidth(),
) {
    item { Spacer(Modifier.width(16.dp)) }
    viewModel.images.value.forEachIndexed { index, image ->
        item {
            Box {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(120.dp)
                        .aspectRatio(viewModel.imageRatio.value.ratio)
                        .clickable { }
                        .background(colorGrey100()),
                    model = image,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image selected to post: ${index + 1}",
                )

                Icon(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(22.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(colorGrey0())
                        .padding(4.dp)
                        .clickable { viewModel.onRemoveImageSelected(index) }
                        .align(Alignment.TopEnd),
                    imageVector = PhosphorIcons.Bold.X,
                    contentDescription = "close image post",
                    tint = colorGrey600(),
                )
            }
            Spacer(Modifier.width(8.dp))
        }
    }
    item { Spacer(Modifier.width(16.dp)) }
}
