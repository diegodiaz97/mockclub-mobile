package com.diego.futty.authentication.profileCreation.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

class KtorRemoteProfileCreationDataSource(
    private val firebaseManager: FirebaseManager
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
}
