package com.diego.futty.core.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.datetime.Clock
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.create
import platform.Foundation.temporaryDirectory
import platform.Foundation.writeToURL
import platform.posix.memcpy

actual class ImageUploader actual constructor() {
    actual suspend fun uploadImage(imageBytes: ByteArray, path: String): String {
        val storageReference = Firebase.storage.reference.child(path)
        storageReference.putFile(createTempFileFromBytes(imageBytes))
        val downloadUrl = storageReference.getDownloadUrl()
        return downloadUrl
    }

    private fun createTempFileFromBytes(imageBytes: ByteArray): File {
        val tempDirectory = NSFileManager.defaultManager.temporaryDirectory
        val tempFilePath =
            tempDirectory.URLByAppendingPathComponent("image_${Clock.System.now()}.jpg")
        val data = imageBytes.toData()

        tempFilePath?.let { data.writeToURL(it, atomically = true) }

        return File(tempFilePath!!)
    }
}


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toData),
        length = this@toData.size.toULong()
    )
}

// La dejo por las dudas, no se usa
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
    }
}
