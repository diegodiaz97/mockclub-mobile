package com.diego.futty.authentication.login.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.firebase.providePassage
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.tweener.passage.model.AppleGatekeeperConfiguration
import com.tweener.passage.model.EmailPasswordGatekeeperConfiguration
import com.tweener.passage.model.Entrant
import com.tweener.passage.model.GoogleGatekeeperAndroidConfiguration
import com.tweener.passage.model.GoogleGatekeeperConfiguration
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

    override suspend fun loginWithGoogle(): DataResult<Entrant, DataError.Remote> {
        val result = firebaseManager.passage.authenticateWithGoogle()

        result.fold(
            onSuccess = { entrant ->
                preferences.saveUserId(entrant.uid)
                preferences.saveUserEmail(entrant.email ?: "")
                return DataResult.Success(entrant)
            },
            onFailure = {
                return DataResult.Error(DataError.Remote.UNKNOWN)
            }
        )
    }

    override suspend fun loginWithApple(): DataResult<Entrant, DataError.Remote> {
        val result = firebaseManager.passage.authenticateWithApple()

        result.fold(
            onSuccess = { entrant ->
                preferences.saveUserId(entrant.uid)
                preferences.saveUserEmail(entrant.email ?: "")
                return DataResult.Success(entrant)
            },
            onFailure = {
                return DataResult.Error(DataError.Remote.UNKNOWN)
            }
        )
    }
}
