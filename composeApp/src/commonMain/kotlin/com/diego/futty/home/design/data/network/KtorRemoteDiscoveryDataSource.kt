package com.diego.futty.home.design.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.remote.safeCall
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.feed.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class KtorRemoteDiscoveryDataSource(
    private val firebaseManager: FirebaseManager,
    private val httpClient: HttpClient,
) : RemoteDiscoveryDataSource {

    private fun baseUrl(): String {
        return if (PlatformInfo.isIOS) {
            "http://192.168.0.192:8080"
        } else {
            "http://10.0.2.2:8080"
        }
    }

    private suspend fun authToken(): String {
        val currentUser = firebaseManager.auth.currentUser
        return currentUser?.getIdToken(false)
            ?: throw IllegalStateException("No token")
    }

    override suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote> =
        safeCall<List<User>> {
            httpClient.get("${baseUrl()}/users/search") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
                parameter("q", query)
                parameter("limit", 10)
                parameter("offset", 0)
            }
        }
}
