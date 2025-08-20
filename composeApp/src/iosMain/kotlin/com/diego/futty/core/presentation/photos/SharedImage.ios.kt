package com.diego.futty.core.presentation.photos

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.jetbrains.skia.Image
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation

actual class SharedImage(private val image: UIImage?) {

    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        if (image == null) return null

        // Elegir formato según transparencia
        val imageData = if (uiImageHasAlpha(image)) {
            UIImagePNGRepresentation(image)
        } else {
            UIImageJPEGRepresentation(image, COMPRESSION_QUALITY)
        } ?: throw IllegalArgumentException("image data is null")

        val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
        val length = imageData.length
        val data: CPointer<ByteVar> = bytes.reinterpret()

        return ByteArray(length.toInt()) { index -> data[index] }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return byteArray?.let { Image.makeFromEncoded(it).toComposeImageBitmap() }
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

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}
