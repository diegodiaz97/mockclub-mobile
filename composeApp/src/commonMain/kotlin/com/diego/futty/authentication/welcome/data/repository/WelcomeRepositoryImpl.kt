package com.diego.futty.authentication.welcome.data.repository

import com.diego.futty.authentication.welcome.data.network.RemoteWelcomeDataSource
import com.diego.futty.authentication.welcome.domain.repository.WelcomeRepository
import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

class WelcomeRepositoryImpl(
    private val remoteWelcomeDataSource: RemoteWelcomeDataSource
) : WelcomeRepository {
    override suspend fun checkUserSession(): DataResult<AuthState, DataError.Remote> {
        return remoteWelcomeDataSource.checkUserSession()
    }

    override suspend fun initializeServerTime() {
        return remoteWelcomeDataSource.initializeServerTime()
    }
}
