package com.diego.futty.core.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.datetime.Clock
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.create
import platform.Foundation.temporaryDirectory
import platform.Foundation.writeToURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation

actual class ImageUploader actual constructor() {
    actual suspend fun uploadImage(imageBytes: ByteArray, path: String): String {
        val uiImage = imageBytes.toUIImage() ?: error("No se pudo decodificar UIImage")
        val hasTransparency = uiImageHasAlpha(uiImage)

        // Convertir a NSData según transparencia
        val (finalData, extension) = if (hasTransparency) {
            Pair(uiImage.PNGRepresentation()!!, ".png")
        } else {
            Pair(uiImage.JPEGRepresentation(0.8)!!, ".jpg")
        }

        // Guardar archivo temporal
        val tempFile = createTempFileFromNSData(finalData, extension)

        // Subir a Firebase Storage
        val storageReference = Firebase.storage.reference.child("$path$extension")
        storageReference.putFile(tempFile)
        val downloadUrl = storageReference.getDownloadUrl()
        return downloadUrl
    }

    private fun createTempFileFromNSData(data: NSData, extension: String): File {
        val tempDirectory = NSFileManager.defaultManager.temporaryDirectory
        val tempFilePath = tempDirectory
            .URLByAppendingPathComponent("image_${Clock.System.now()}$extension")

        data.writeToURL(tempFilePath!!, atomically = true)
        return File(tempFilePath)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun uiImageHasAlpha(image: UIImage): Boolean {
        val cgImage = image.CGImage ?: return false
        val alphaInfo = CGImageGetAlphaInfo(cgImage)
        return when (alphaInfo) {
            CGImageAlphaInfo.kCGImageAlphaFirst,
            CGImageAlphaInfo.kCGImageAlphaLast,
            CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst,
            CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> true
            else -> false
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toUIImage(): UIImage? = memScoped {
    val data = NSData.create(
        bytes = allocArrayOf(this@toUIImage),
        length = this@toUIImage.size.toULong()
    )
    return UIImage(data = data)
}

// Helpers para NSData desde UIImage
fun UIImage.PNGRepresentation(): NSData? = UIImagePNGRepresentation(this)
fun UIImage.JPEGRepresentation(quality: Double): NSData? = UIImageJPEGRepresentation(this, quality)
