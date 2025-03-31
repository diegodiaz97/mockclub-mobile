package com.diego.futty.setup.profile.presentation.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.photos.PermissionCallback
import com.diego.futty.core.presentation.photos.PermissionStatus
import com.diego.futty.core.presentation.photos.PermissionType
import com.diego.futty.core.presentation.photos.createPermissionsManager
import com.diego.futty.core.presentation.photos.rememberCameraManager
import com.diego.futty.core.presentation.photos.rememberGalleryManager
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
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.flowrow.MultipleFlowList
import com.diego.futty.home.design.presentation.component.progressbar.CircularProgressBar
import com.diego.futty.home.design.presentation.component.progressbar.LinearProgressBar
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        ImageHandler(viewModel)
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
            val image = user.profileImage?.image
            Avatar.ProfileAvatar(
                modifier = Modifier.align(Alignment.Top),
                imageUrl = viewModel.urlImage.value,
                initials = user.profileImage?.initials,
                background = user.profileImage?.background?.toColor(),
                avatarSize = AvatarSize.Extra,
                onClick = { viewModel.showUpdateImage(true) }
            ).Draw()
            Column(verticalArrangement = Arrangement.SpaceAround) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = user.name ?: "No especificado",
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorGrey900()
                )
                if (user.description != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = user.description,
                        style = typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorGrey400()
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "En Futty desde el ${user.creationDate}",
                    style = typography.titleSmall,
                    fontWeight = FontWeight.Normal,
                    color = colorGrey400()
                )
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

/*TODO - MEJORAR ESTO*/
@Composable
private fun ImageHandler(
    viewModel: ProfileViewModel,
    /*showOptions: Boolean,
    launchCamera: Boolean,
    launchGallery: Boolean,
    launchSettings: Boolean,*/
) {
    val coroutineScope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var imageSourceOptionDialog by remember { mutableStateOf(value = false) } // SHOW OPTIONS
    var launchCamera by remember { mutableStateOf(value = false) } // LANZA CAMARA
    var launchGallery by remember { mutableStateOf(value = false) } // LANZA GALERIA
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
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
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
        Column(modifier = Modifier.navigationBarsPadding()) {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                title = "Crear cuenta",
                isEnabled = true,
                onClick = {
                    imageSourceOptionDialog = false
                    viewModel.showUpdateImage(false)
                    launchGallery = true
                }
            )
            SecondaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                title = "Ya tengo cuenta",
                isEnabled = true,
                onClick = {
                    imageSourceOptionDialog = false
                    viewModel.showUpdateImage(false)
                    launchCamera = true
                }
            )
        }
    }
    if (launchGallery) { // ABRIR GALERÍA
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }
    if (launchCamera) { // ABRIR CAMARA
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
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
