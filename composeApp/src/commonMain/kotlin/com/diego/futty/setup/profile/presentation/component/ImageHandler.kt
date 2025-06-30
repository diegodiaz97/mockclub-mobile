package com.diego.futty.setup.profile.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.diego.futty.core.presentation.photos.PermissionCallback
import com.diego.futty.core.presentation.photos.PermissionStatus
import com.diego.futty.core.presentation.photos.PermissionType
import com.diego.futty.core.presentation.photos.createPermissionsManager
import com.diego.futty.core.presentation.photos.rememberCameraManager
import com.diego.futty.core.presentation.photos.rememberGalleryManager
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileImageHandler(viewModel: ProfileViewModel) {
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
        OpenedProfileImage(
            userId = viewModel.userId.value.isEmpty(),
            image = viewModel.urlImage.value,
            initials = viewModel.initials.value,
            background = viewModel.user.value?.profileImage?.background?.toColor(),
            showUpdateImage = { viewModel.showUpdateImage() },
            onLaunchCamera = { viewModel.launchCamera() },
            onLaunchGallery = { viewModel.launchGallery() },
        )
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
