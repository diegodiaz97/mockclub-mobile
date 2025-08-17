package com.diego.futty.home.design.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.remote.safeCall
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.design.presentation.util.SearchType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
            //"http://192.168.0.10:8080" //Casa de mama
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

    override suspend fun searchPosts(
        currentUserId: String,
        query: String,
        type: SearchType,
        limit: Int,
        offset: Int
    ): DataResult<List<PostWithExtras>, DataError.Remote> =
        safeCall {
            httpClient.get("${baseUrl()}/search/posts/${type.path}") {
                parameter("currentUserId", currentUserId)
                parameter("q", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }.body()
        }
}
