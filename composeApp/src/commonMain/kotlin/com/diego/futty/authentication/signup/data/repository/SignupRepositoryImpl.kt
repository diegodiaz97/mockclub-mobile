package com.diego.futty.authentication.signup.data.repository

import com.diego.futty.authentication.signup.data.network.RemoteSignupDataSource
import com.diego.futty.authentication.signup.domain.repository.SignupRepository
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

class SignupRepositoryImpl(
    private val remoteSignupDataSource: RemoteSignupDataSource
) : SignupRepository {

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): DataResult<String, DataError.Remote> {
        return remoteSignupDataSource.signUpWithEmail(email, password)
    }
}
