package com.diego.futty.authentication.signup.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

interface RemoteSignupDataSource {
    suspend fun signUpWithEmail(email: String, password: String): DataResult<String, DataError.Remote>
}
