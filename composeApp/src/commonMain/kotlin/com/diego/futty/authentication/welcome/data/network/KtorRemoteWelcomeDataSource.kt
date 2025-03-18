package com.diego.futty.authentication.welcome.data.network

import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

class KtorRemoteWelcomeDataSource(
    private val firebaseManager: FirebaseManager,
) : RemoteWelcomeDataSource {
    override suspend fun checkUserSession(): DataResult<AuthState, DataError.Remote> {
        return try {
            if (firebaseManager.auth.currentUser != null) {
                DataResult.Success(AuthState.LoggedIn)
            } else {
                DataResult.Success(AuthState.LoggedOut)
            }
        } catch (e: Exception) {
            DataResult.Success(AuthState.LoggedOut)
        }
    }
}
