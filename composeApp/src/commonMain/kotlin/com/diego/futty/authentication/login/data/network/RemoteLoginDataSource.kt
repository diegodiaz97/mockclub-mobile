package com.diego.futty.authentication.login.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import dev.gitlive.firebase.auth.AuthResult

interface RemoteLoginDataSource {
    suspend fun loginWithEmail(email: String, password: String): DataResult<AuthResult, DataError.Remote>
}
