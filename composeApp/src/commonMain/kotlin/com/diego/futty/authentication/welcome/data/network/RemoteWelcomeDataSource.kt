package com.diego.futty.authentication.welcome.data.network

import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

interface RemoteWelcomeDataSource {
    suspend fun checkUserSession(): DataResult<AuthState, DataError.Remote>
    suspend fun initializeServerTime()
}
