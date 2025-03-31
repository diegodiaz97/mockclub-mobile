package com.diego.futty.authentication.profileCreation.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.firebase.ImageUploader
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

class KtorRemoteProfileCreationDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemoteProfileCreationDataSource {

    override suspend fun createProfile(user: User): DataResult<String, DataError.Remote> {
        return try {
            val firestore = firebaseManager.firestore
            firestore.collection("users").document(user.id).set(user)
            DataResult.Success("Perfil actualizado correctamente")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun fetchProfile(id: String): DataResult<User, DataError.Remote> {
        return try {
            val firestore = firebaseManager.firestore
            val document = firestore.collection("users").document(id).get()
            DataResult.Success(document.data())
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun updateProfile(
        id: String,
        updates: Map<String, Any?>
    ): DataResult<String, DataError.Remote> {
        return try {
            val firestore = firebaseManager.firestore
            firestore.collection("users").document(id).update(updates)
            DataResult.Success("Perfil actualizado correctamente")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun updateProfileImage(image: ByteArray): DataResult<String, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val imagePath = "profile_images/$userId.jpg"
            val url = ImageUploader().uploadImage(image, imagePath)

            val updates: MutableMap<String, String> = mutableMapOf()
            updates["profileImage.image"] = url

            val firestore = firebaseManager.firestore
            firestore.collection("users").document(userId).update(updates)

            DataResult.Success(url)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}
