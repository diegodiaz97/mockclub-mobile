package com.diego.futty.core.data.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

actual class ImageUploader actual constructor() {

    actual suspend fun uploadImage(imageBytes: ByteArray, path: String): String {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        // Detectar si tiene transparencia
        val hasTransparency = bitmapHasAlpha(bitmap)

        // Convertir con el formato adecuado
        val finalBytes = convertImage(bitmap, hasTransparency)

        // Guardar archivo temporal con extensión correcta
        val extension = if (hasTransparency) ".png" else ".webp"
        val tempFile = createTempFileFromBytes(finalBytes, extension)

        // Subir a Firebase Storage
        val storageReference: StorageReference = Firebase.storage.reference.child("$path$extension")
        val uri = Uri.fromFile(tempFile)
        storageReference.putFile(uri).await()

        // Obtener URL de descarga
        return storageReference.downloadUrl.await().toString()
    }

    private fun createTempFileFromBytes(imageBytes: ByteArray, extension: String): File {
        val tempFile = File.createTempFile("image_", extension)
        FileOutputStream(tempFile).use { it.write(imageBytes) }
        return tempFile
    }

    private fun bitmapHasAlpha(bitmap: Bitmap): Boolean {
        if (!bitmap.hasAlpha()) return false
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels.any { (it ushr 24) and 0xFF < 255 }
    }

    private fun convertImage(bitmap: Bitmap, hasTransparency: Boolean): ByteArray {
        val outputStream = ByteArrayOutputStream()

        if (hasTransparency) {
            // Transparencia → WebP Lossless (API 30+) o PNG
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, outputStream)
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        } else {
            // Sin transparencia → WebP Lossy
            val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Bitmap.CompressFormat.WEBP_LOSSY
            } else {
                Bitmap.CompressFormat.WEBP
            }
            bitmap.compress(format, 80, outputStream)
        }

        return outputStream.toByteArray()
    }
}
