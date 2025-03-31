package com.diego.futty.core.data.firebase

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream


actual class ImageUploader actual constructor() {
    actual suspend fun uploadImage(imageBytes: ByteArray, path: String): String {
        // Crear un archivo temporal en el almacenamiento
        val tempFile = createTempFileFromBytes(imageBytes)

        // Crear una referencia a Firebase Storage
        val storageReference: StorageReference = Firebase.storage.reference.child(path)

        // Subir el archivo
        val uri = Uri.fromFile(tempFile)
        storageReference.putFile(uri).await()  // Uso de putFile

        // Obtener la URL de la imagen subida
        val downloadUrl = storageReference.downloadUrl.await()
        return downloadUrl.toString()
    }

    private fun createTempFileFromBytes(imageBytes: ByteArray): File {
        // Crear un archivo temporal
        val tempFile = File.createTempFile("image_", ".jpg")

        // Escribir los bytes en el archivo temporal
        FileOutputStream(tempFile).use { it.write(imageBytes) }

        return tempFile
    }
}
