package com.diego.futty.authentication.signup.data.network

import com.diego.futty.core.data.FirebaseManager
import com.diego.futty.core.data.responseToResult
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class KtorRemoteSignupDataSource(
    private val firebaseManager: FirebaseManager
) : RemoteSignupDataSource {

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): DataResult<String, DataError.Remote> {
        return try {
            firebaseManager.auth.createUserWithEmailAndPassword(email, password)
            DataResult.Success("Email: $email\nregistrado correctamente")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }


    suspend inline fun <reified T> safeCall(
        execute: () -> HttpResponse
    ): DataResult<T, DataError.Remote> {
        val response = try {
            execute()
        } catch (e: SocketTimeoutException) {
            return DataResult.Error(DataError.Remote.REQUEST_TIMEOUT)
        } catch (e: UnresolvedAddressException) {
            return DataResult.Error(DataError.Remote.NO_INTERNET)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            return DataResult.Error(DataError.Remote.UNKNOWN)
        }

        return responseToResult(response)
    }
}
