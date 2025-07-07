package com.diego.futty.authentication.welcome.domain.repository

import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

interface WelcomeRepository {
    suspend fun checkUserSession(): DataResult<AuthState, DataError.Remote>
    suspend fun initializeServerTime()
}
