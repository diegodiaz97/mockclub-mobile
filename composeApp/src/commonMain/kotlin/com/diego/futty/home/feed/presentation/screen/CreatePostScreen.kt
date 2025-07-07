package com.diego.futty.home.feed.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.diego.futty.core.presentation.photos.CameraView
import com.diego.futty.core.presentation.theme.colorAlertLight
import com.diego.futty.core.presentation.theme.colorErrorLight
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey400
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
import com.diego.futty.home.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.chip.Chip
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressSize
import com.diego.futty.home.design.presentation.component.progressbar.DynamicCircularProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.post.presentation.viewmodel.PostViewModel
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import compose.icons.TablerIcons
import compose.icons.tablericons.Camera
import compose.icons.tablericons.Photo
import compose.icons.tablericons.Plus
import compose.icons.tablericons.X

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePostScreen(
    viewModel: PostViewModel,
    user: User?,
    onClose: () -> Unit,
    onStartPostCreation: () -> Unit,
    onPostCreated: () -> Unit,
) {
    BackHandler {
        onClose()
        viewModel.dismissPostCreation()
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
                        imageUrl = user.profileImage.image,
                        initials = user.profileImage.initials,
                        background = user.profileImage.background.toColor(),
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

    if (viewModel.showTags.value) {
        AddTags(
            existingTags = viewModel.existingTags.value,
            selectedTags = viewModel.newTags.value,
            onDismiss = { viewModel.showTags() },
            onConfirm = { tags -> viewModel.addTags(tags) },
        )
    }

    CameraView(
        showGallery = viewModel.launchGallery.value,
        showCamera = viewModel.launchCamera.value,
        launchCamera = { viewModel.launchCamera() },
        launchGallery = { viewModel.launchGallery() },
        onImageCaptured = { image ->
            viewModel.onImagesSelected(listOf(image))
        }
    )
}

fun Modifier.adaptiveBottomBarPadding(imeVisible: Boolean): Modifier {
    return if (imeVisible) {
        if (PlatformInfo.isIOS) this else this.padding(bottom = 12.dp)
    } else {
        this.navigationBarsPadding()
    }
}

@Composable
private fun PostFooter(viewModel: PostViewModel) {
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            //.background(if (imeVisible) colorError() else colorAlert())
            .adaptiveBottomBarPadding(imeVisible),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val tags = viewModel.newTags.value

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
                                color = colorSecondary(),
                                isSelected = true,
                            ) { viewModel.removeTag(index) }
                            Avatar.IconAvatar(
                                modifier = Modifier.offset(x = (-14).dp, y = (-8).dp),
                                icon = TablerIcons.X,
                                tint = colorGrey600(),
                                background = colorGrey0(),
                                avatarSize = AvatarSize.Atomic,
                                onClick = { viewModel.removeTag(index) }
                            ).Draw()
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = viewModel.images.value.isNotEmpty()
        ) {
            SelectedImages(viewModel)
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

            CameraView(viewModel)
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
private fun CameraView(viewModel: PostViewModel) {
    Avatar.IconAvatar(
        modifier = Modifier,
        icon = TablerIcons.Camera,
        tint = colorGrey900(),
        background = colorGrey0(),
        avatarSize = AvatarSize.Small,
        onClick = { viewModel.launchCamera() }
    ).Draw()
}

@Composable
fun GalleryView(viewModel: PostViewModel) {
    val scope = rememberCoroutineScope()

    val multipleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Multiple(maxSelection = 4),
        scope = scope,
        onResult = { images ->
            viewModel.onImagesSelected(images)
        }
    )

    Avatar.IconAvatar(
        modifier = Modifier,
        icon = TablerIcons.Photo,
        tint = colorGrey900(),
        background = colorGrey0(),
        avatarSize = AvatarSize.Small,
        onClick = { multipleImagePicker.launch() }
    ).Draw()
}

@Composable
fun SelectedImages(viewModel: PostViewModel) = LazyRow(
    modifier = Modifier.height(80.dp).fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    item { Spacer(Modifier.width(16.dp)) }
    viewModel.images.value.forEachIndexed { index, image ->
        item {
            Box {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { }
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorGrey100()),
                    model = image,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image selected to post: ${index + 1}",
                )
                Avatar.IconAvatar(
                    modifier = Modifier.padding(4.dp).align(Alignment.TopEnd),
                    icon = TablerIcons.X,
                    tint = colorGrey600(),
                    background = colorGrey0(),
                    avatarSize = AvatarSize.Atomic,
                    onClick = {
                        viewModel.onRemoveImageSelected(index)
                    }
                ).Draw()
            }
        }
    }
    item { Spacer(Modifier.width(16.dp)) }
}

@Composable
fun AddTags(
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
            text = "Agregar tags",
            style = typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )

        if (existingTags.isNotEmpty()) {
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
                                text = tag,
                                color = colorSecondary(),
                                isSelected = true,
                            ) { tags = tags.filterIndexed { i, _ -> i != index } }
                            Avatar.IconAvatar(
                                modifier = Modifier.offset(x = (-14).dp, y = (-8).dp),
                                icon = TablerIcons.X,
                                tint = colorGrey600(),
                                background = colorGrey0(),
                                avatarSize = AvatarSize.Atomic,
                                onClick = {
                                    tags = tags.filterIndexed { i, _ -> i != index }
                                }
                            ).Draw()
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
                onFocusChanged = { },
                onTextChangeAction = {
                    if (it.length <= 12) {
                        newTag = it.lowercase().trim()
                    }
                }
            ).Draw()
            Avatar.IconAvatar(
                icon = TablerIcons.Plus,
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
