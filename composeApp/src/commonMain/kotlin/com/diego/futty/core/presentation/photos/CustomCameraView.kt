package com.diego.futty.core.presentation.photos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CameraView(
    showGallery: Boolean,
    showCamera: Boolean,
    launchCamera: () -> Unit,
    launchGallery: () -> Unit,
    onImageCaptured: (image: ByteArray) -> Unit,
) {
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
                        PermissionType.CAMERA -> { launchCamera() }
                        PermissionType.GALLERY -> { launchGallery() }
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
            delay(200)
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

    if (showGallery) { // ABRIR GALERÍA
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery()
    }

    if (showCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            LaunchedEffect("camera-launch") {
                cameraManager.launch()
            }
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera()
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
    if (imageByteArray != null) {
        onImageCaptured(imageByteArray!!)
        imageByteArray = null
    }
}
