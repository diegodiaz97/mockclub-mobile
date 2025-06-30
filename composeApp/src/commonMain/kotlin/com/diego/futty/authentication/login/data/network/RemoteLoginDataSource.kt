package com.diego.futty.authentication.login.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.tweener.passage.model.Entrant
import dev.gitlive.firebase.auth.AuthResult

interface RemoteLoginDataSource {
    suspend fun loginWithEmail(email: String, password: String): DataResult<AuthResult, DataError.Remote>
    suspend fun loginWithGoogle(): DataResult<Entrant, DataError.Remote>
    suspend fun loginWithApple(): DataResult<Entrant, DataError.Remote>
}
