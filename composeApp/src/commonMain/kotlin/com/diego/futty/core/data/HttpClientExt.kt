package com.diego.futty.core.data

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): DataResult<T, DataError.Remote> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return DataResult.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        return DataResult.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return DataResult.Error(DataError.Remote.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): DataResult<T, DataError.Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                DataResult.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                DataResult.Error(DataError.Remote.SERIALIZATION)
            }
        }
        408 -> DataResult.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> DataResult.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> DataResult.Error(DataError.Remote.SERVER)
        else -> DataResult.Error(DataError.Remote.UNKNOWN)
    }
}