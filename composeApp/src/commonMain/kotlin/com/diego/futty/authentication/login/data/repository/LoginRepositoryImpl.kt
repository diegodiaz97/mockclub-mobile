package com.diego.futty.authentication.login.data.repository

import com.diego.futty.authentication.login.data.network.RemoteLoginDataSource
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import dev.gitlive.firebase.auth.AuthResult

class LoginRepositoryImpl(
    private val remoteLoginDataSource: RemoteLoginDataSource
) : LoginRepository {

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): DataResult<AuthResult, DataError.Remote> {
        return remoteLoginDataSource.loginWithEmail(email, password)
    }
}