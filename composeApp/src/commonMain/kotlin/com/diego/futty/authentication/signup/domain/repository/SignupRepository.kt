package com.diego.futty.authentication.signup.domain.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

interface SignupRepository {
    suspend fun signUpWithEmail(
        email: String,
        password: String
    ): DataResult<String, DataError.Remote>
}