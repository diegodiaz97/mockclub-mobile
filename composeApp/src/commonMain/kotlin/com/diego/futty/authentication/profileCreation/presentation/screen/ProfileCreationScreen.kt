package com.diego.futty.authentication.profileCreation.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.X
import com.diego.futty.authentication.profileCreation.presentation.viewmodel.ProfileCreationViewModel
import com.diego.futty.core.presentation.photos.PermissionCallback
import com.diego.futty.core.presentation.photos.PermissionStatus
import com.diego.futty.core.presentation.photos.PermissionType
import com.diego.futty.core.presentation.photos.createPermissionsManager
import com.diego.futty.core.presentation.photos.rememberCameraManager
import com.diego.futty.core.presentation.photos.rememberGalleryManager
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileCreationScreen(viewModel: ProfileCreationViewModel) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Tus datos",
                topBarActionType = TopBarActionType.Icon(
                    icon = PhosphorIcons.Bold.X,
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
            onTextChangeAction = { viewModel.updateName(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.lastName.value,
            label = "Apellido",
            placeholder = viewModel.lastName.value,
            onTextChangeAction = { viewModel.updateLastName(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.description.value,
            label = "Descripción (opcional)",
            placeholder = viewModel.description.value,
            onTextChangeAction = { viewModel.updateDescription(it) }
        ).Draw()

        TextInput.Input(
            input = viewModel.country.value,
            label = "Nacionalidad",
            placeholder = viewModel.country.value,
            onTextChangeAction = { viewModel.updateCountry(it) }
        ).Draw()

        AnimatedVisibility(viewModel.banner.value != null) {
            viewModel.banner.value?.Draw()
        }
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
                if (viewModel.user.value?.userType == USER_TYPE_PRO) {
                    VerifiedIcon(Modifier.padding(top = 4.dp))
                }
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
        Banner.StatusBanner(
            bannerUIData = BannerUIData(
                title = "Algo salió mal",
                description = "Brinda servicios de  Configuración del dispositivo.",
                status = BannerStatus.Error,
                action = {
                    permissionRationalDialog = false
                    launchSetting = true
                }
            ),
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
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape),
                            shimmerModifier = Modifier
                                .padding(horizontal = 24.dp)
                                .clip(CircleShape),
                            contentDescription = "profile image",
                            image = viewModel.urlImage.value
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SecondaryButton(
                        title = "Elegir de la galería",
                        color = colorGrey900(),
                        onClick = {
                            viewModel.showUpdateImage()
                            viewModel.launchGallery()
                        }
                    )
                    SecondaryButton(
                        title = "Tomar foto",
                        color = colorGrey900(),
                        onClick = {
                            viewModel.showUpdateImage()
                            viewModel.launchCamera()
                        }
                    )
                }
            }
            Avatar.IconAvatar(
                modifier = Modifier
                    .padding(top = if (PlatformInfo.isAndroid) 12.dp else 60.dp, end = 16.dp)
                    .align(Alignment.TopEnd),
                icon = PhosphorIcons.Bold.X,
                background = colorGrey100(),
                onClick = { viewModel.showUpdateImage() }
            ).Draw()
        }
    }
}
