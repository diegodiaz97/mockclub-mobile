package com.diego.futty.setup.settings.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

class KtorRemoteLogoutDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
): RemoteLogoutDataSource {
    override suspend fun logout(): DataResult<String, DataError.Remote> {
        return try {
            firebaseManager.auth.signOut()
            preferences.saveUserId("")
            preferences.saveUserEmail("")
            DataResult.Success("")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}