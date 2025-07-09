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
        // Convertir a WebP antes de guardar
        val webpBytes = convertToWebP(imageBytes)

        // Guardar archivo temporal con extensión .webp
        val tempFile = createTempFileFromBytes(webpBytes, extension = ".webp")

        // Crear una referencia a Firebase Storage
        val storageReference: StorageReference = Firebase.storage.reference.child(path)

        // Subir el archivo
        val uri = Uri.fromFile(tempFile)
        storageReference.putFile(uri).await()  // Uso de putFile

        // Obtener la URL de la imagen subida
        val downloadUrl = storageReference.downloadUrl.await()
        return downloadUrl.toString()
    }

    private fun createTempFileFromBytes(imageBytes: ByteArray, extension: String = ".jpg"): File {
        val tempFile = File.createTempFile("image_", extension)
        FileOutputStream(tempFile).use { it.write(imageBytes) }
        return tempFile
    }

    private fun convertToWebP(originalBytes: ByteArray, quality: Int = 70): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
        val outputStream = ByteArrayOutputStream()
        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }

        bitmap.compress(format, quality, outputStream)
        return outputStream.toByteArray()
    }
}
