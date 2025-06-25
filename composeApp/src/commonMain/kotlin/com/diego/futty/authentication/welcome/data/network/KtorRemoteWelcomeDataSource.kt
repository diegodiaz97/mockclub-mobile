package com.diego.futty.authentication.welcome.data.network

import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

class KtorRemoteWelcomeDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemoteWelcomeDataSource {
    override suspend fun checkUserSession(): DataResult<AuthState, DataError.Remote> {
        return try {
            val user = preferences.getUserId()
            if (firebaseManager.auth.currentUser != null && user.isNullOrBlank().not()) {
                DataResult.Success(AuthState.LoggedIn)
            } else {
                DataResult.Success(AuthState.LoggedOut)
            }
        } catch (e: Exception) {
            DataResult.Success(AuthState.LoggedOut)
        }
    }
}
