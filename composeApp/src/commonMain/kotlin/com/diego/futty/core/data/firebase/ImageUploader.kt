package com.diego.futty.core.data.firebase

expect class ImageUploader() {
    suspend fun uploadImage(imageBytes: ByteArray, path: String): String
}
