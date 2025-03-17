package com.diego.futty.authentication.login.data.network

import com.diego.futty.core.data.FirebaseManager
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import dev.gitlive.firebase.auth.AuthResult

class KtorRemoteLoginDataSource(
    private val firebaseManager: FirebaseManager
) : RemoteLoginDataSource {

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): DataResult<AuthResult, DataError.Remote> {
        return try {
            val user = firebaseManager.auth.signInWithEmailAndPassword(email, password)
            DataResult.Success(user)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}