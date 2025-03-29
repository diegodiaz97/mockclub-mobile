package com.diego.futty.authentication.signup.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

class KtorRemoteSignupDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemoteSignupDataSource {

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): DataResult<String, DataError.Remote> {
        return try {
            firebaseManager.auth.createUserWithEmailAndPassword(email, password)
            preferences.saveUserId(firebaseManager.auth.currentUser?.uid ?: "")
            preferences.saveUserEmail(firebaseManager.auth.currentUser?.email ?: "")
            DataResult.Success("Email: $email\nregistrado correctamente")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}
