package com.diego.futty.authentication.login.data.repository

import com.diego.futty.authentication.login.data.network.RemoteLoginDataSource
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.tweener.passage.model.Entrant
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

    override suspend fun loginWithGoogle(): DataResult<Entrant, DataError.Remote> {
        return remoteLoginDataSource.loginWithGoogle()
    }

    override suspend fun loginWithApple(): DataResult<Entrant, DataError.Remote> {
        return remoteLoginDataSource.loginWithApple()
    }
}
