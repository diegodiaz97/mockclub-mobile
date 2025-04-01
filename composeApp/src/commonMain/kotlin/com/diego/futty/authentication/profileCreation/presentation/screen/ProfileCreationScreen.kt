package com.diego.futty.authentication.profileCreation.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.diego.futty.authentication.profileCreation.presentation.viewmodel.ProfileCreationViewModel
import com.diego.futty.core.presentation.photos.PermissionCallback
import com.diego.futty.core.presentation.photos.PermissionStatus
import com.diego.futty.core.presentation.photos.PermissionType
import com.diego.futty.core.presentation.photos.createPermissionsManager
import com.diego.futty.core.presentation.photos.rememberCameraManager
import com.diego.futty.core.presentation.photos.rememberGalleryManager
import com.diego.futty.core.presentation.theme.Shimmer
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.HideKeyboard
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import compose.icons.TablerIcons
import compose.icons.tablericons.X
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileCreationScreen(viewModel: ProfileCreationViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize().clickable { viewModel.hideKeyboard() },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Tus datos",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.X,
                    onClick = { viewModel.onCloseClicked() }
                )
            )
        },
        content = { paddingValues ->
            ProfileCreationContent(viewModel, paddingValues)
        },
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding()) {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    title = "Actualizar",
                    isEnabled = viewModel.canContinue.value,
                    onClick = { viewModel.onContinueClicked() }
                )
            }
        }
    )
    ImageHandler(viewModel)
}

@Composable
private fun ProfileCreationContent(
    viewModel: ProfileCreationViewModel,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { viewModel.hideKeyboard() }
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MainInfo(viewModel)

        TextInput.Input(
            input = viewModel.name.value,
            label = "Nombre",
            placeholder = viewModel.name.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateName(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.lastName.value,
            label = "Apellido",
            placeholder = viewModel.lastName.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateLastName(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.description.value,
            label = "Descripción (opcional)",
            placeholder = viewModel.description.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateDescription(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.country.value,
            label = "Nacionalidad",
            placeholder = viewModel.country.value,
            onFocusChanged = { viewModel.hideKeyboard() },
            onTextChangeAction = { viewModel.updateCountry(it) }
        ).Draw()

        AnimatedVisibility(viewModel.banner.value != null) {
            viewModel.banner.value?.Draw()
        }
    }

    if (viewModel.hideKeyboard.value) {
        HideKeyboard()
    }
}

@Composable
private fun MainInfo(viewModel: ProfileCreationViewModel) {
    Row(
        modifier = Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar.ProfileAvatar(
            modifier = Modifier.align(Alignment.Top),
            imageUrl = viewModel.urlImage.value,
            initials = viewModel.initials.value,
            background = viewModel.background.value.toColor(),
            avatarSize = AvatarSize.Extra,
            onClick = { viewModel.showUpdateImage() }
        ).Draw()
        Column(verticalArrangement = Arrangement.SpaceAround) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 1.dp),
                    text = "${viewModel.name.value} ${viewModel.lastName.value}",
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorGrey900()
                )
                VerifiedIcon(Modifier.padding(top = 4.dp))
            }
            Text(
                modifier = Modifier.fillMaxWidth().padding(start = 1.dp),
                text = viewModel.description.value,
                style = typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey500()
            )
        }
    }
}

/*TODO - MEJORAR ESTO*/
@Composable
private fun ImageHandler(viewModel: ProfileCreationViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var launchSetting by remember { mutableStateOf(value = false) } // LANZA SETTINGS
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> viewModel.launchCamera()
                        PermissionType.GALLERY -> viewModel.launchGallery()
                    }
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }
    })

    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            val byteArray = withContext(Dispatchers.Default) {
                it?.toByteArray()
            }
            imageBitmap = bitmap
            imageByteArray = byteArray
        }
    }

    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            val byteArray = withContext(Dispatchers.Default) {
                it?.toByteArray()
            }
            imageBitmap = bitmap
            imageByteArray = byteArray
        }
    }

    // MOSTRAR OPCIONES (CAMARA Y GALERIA)
    if (viewModel.showUpdateImage.value) {
        imageBitmap = null
        OpenedImage(viewModel)
    }
    if (viewModel.launchGallery.value) { // ABRIR GALERÍA
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        viewModel.launchGallery()
    }
    if (viewModel.launchCamera.value) { // ABRIR CAMARA
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        viewModel.launchCamera()
    }
    if (launchSetting) { // IR A SETTINGS
        permissionsManager.launchSettings()
        launchSetting = false
    }
    if (permissionRationalDialog) { // ERROR NO ACEPTO PERMISOS
        Banner.ClickableBanner(
            title = "Algo salió mal",
            subtitle = "Éste banner te avisará cuando algo salió mal.",
            onClick = {
                permissionRationalDialog = false
                launchSetting = true
            }
        ).Draw()
    }
    if (imageBitmap != null) {
        viewModel.updateImage(imageBitmap!!, imageByteArray!!)
    }
}

@Composable
private fun OpenedImage(viewModel: ProfileCreationViewModel) {
    FlexibleBottomSheet(
        containerColor = colorGrey0().copy(alpha = 0.9f),
        scrimColor = Color.Transparent,
        onDismissRequest = { viewModel.showUpdateImage() },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 1f
            ),
            isModal = true,
            skipIntermediatelyExpanded = true,
            skipSlightlyExpanded = true,
        ),
        windowInsets = WindowInsets(0.dp),
        dragHandle = {},
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    viewModel.urlImage.value?.isNotEmpty() == true -> {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape),
                            model = viewModel.urlImage.value,
                            contentScale = ContentScale.Crop,
                            contentDescription = "profile image",
                            loading = {
                                Shimmer(
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.Center)
                                )
                            }
                        )
                    }

                    viewModel.initials.value != null -> {
                        val color = viewModel.background.value.toColor()
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 50.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.initials.value ?: "",
                                fontWeight = FontWeight.Bold,
                                style = typography.headlineLarge,
                                color = colorGrey900(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                Row(modifier = Modifier.padding(20.dp)) {
                    SecondaryButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        title = "Tomar foto",
                        onClick = {
                            viewModel.showUpdateImage()
                            viewModel.launchCamera()
                        }
                    )
                    SecondaryButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        title = "Seleccionar foto",
                        onClick = {
                            viewModel.showUpdateImage()
                            viewModel.launchGallery()
                        }
                    )
                }
            }
            Avatar.IconAvatar(
                modifier = Modifier
                    .padding(top = if (PlatformInfo.isAndroid) 12.dp else 60.dp, end = 16.dp)
                    .align(Alignment.TopEnd),
                icon = TablerIcons.X,
                background = colorGrey100(),
                onClick = { viewModel.showUpdateImage() }
            ).Draw()
        }
    }
}
