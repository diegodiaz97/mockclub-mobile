package com.diego.futty.authentication.welcome.data.network

import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.tweener.passage.model.AppleGatekeeperConfiguration
import com.tweener.passage.model.EmailPasswordGatekeeperConfiguration
import com.tweener.passage.model.GoogleGatekeeperAndroidConfiguration
import com.tweener.passage.model.GoogleGatekeeperConfiguration
import dev.gitlive.firebase.Firebase

class KtorRemoteWelcomeDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemoteWelcomeDataSource {

    override suspend fun checkUserSession(): DataResult<AuthState, DataError.Remote> {
        return try {
            initializePassage()

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

    private fun initializePassage() {
        val gatekeeperConfigurations = listOf(
            GoogleGatekeeperConfiguration(
                serverClientId = "890310423891-rsfq9fna4o856368httkdbo2dues69f9.apps.googleusercontent.com",
                android = GoogleGatekeeperAndroidConfiguration(
                    filterByAuthorizedAccounts = false,
                    autoSelectEnabled = true,
                    maxRetries = 3
                )
            ),
            AppleGatekeeperConfiguration(),
            EmailPasswordGatekeeperConfiguration
        )

        firebaseManager.passage.initialize(gatekeeperConfigurations, Firebase)
    }
}
