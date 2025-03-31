package com.diego.futty.authentication.login.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import dev.gitlive.firebase.auth.AuthResult

class KtorRemoteLoginDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemoteLoginDataSource {

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): DataResult<AuthResult, DataError.Remote> {
        return try {
            val user = firebaseManager.auth.signInWithEmailAndPassword(email, password)
            preferences.saveUserId(user.user?.uid ?: "")
            preferences.saveUserEmail(user.user?.email ?: "")
            DataResult.Success(user)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}