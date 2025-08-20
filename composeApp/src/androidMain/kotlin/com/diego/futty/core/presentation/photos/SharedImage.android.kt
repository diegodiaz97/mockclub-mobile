package com.diego.futty.core.presentation.photos

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual class SharedImage(private val bitmap: android.graphics.Bitmap?) {

    actual fun toByteArray(): ByteArray? {
        if (bitmap == null) return null

        val byteArrayOutputStream = ByteArrayOutputStream()
        val format = if (bitmap.hasAlpha()) {
            android.graphics.Bitmap.CompressFormat.PNG
        } else {
            android.graphics.Bitmap.CompressFormat.JPEG
        }

        val quality = if (format == android.graphics.Bitmap.CompressFormat.JPEG) 99 else 100
        bitmap.compress(format, quality, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return byteArray?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
        }
    }
}
