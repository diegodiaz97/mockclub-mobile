package com.diego.futty.setup.profile.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.remote.safeCall
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.feed.domain.model.Count
import com.diego.futty.home.feed.domain.model.Following
import com.diego.futty.home.feed.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders

class KtorRemoteProfileDataSource(
    private val firebaseManager: FirebaseManager,
    private val httpClient: HttpClient,
) : RemoteProfileDataSource {

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

    override suspend fun followUser(targetUserId: String): DataResult<Unit, DataError.Remote> =
        safeCall {
            httpClient.post("${baseUrl()}/users/$targetUserId/follow") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            }
        }

    override suspend fun unfollowUser(targetUserId: String): DataResult<Unit, DataError.Remote> =
        safeCall {
            httpClient.delete("${baseUrl()}/users/$targetUserId/follow") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            }
        }

    override suspend fun areYouFollowing(
        followerId: String,
        followingId: String
    ): DataResult<Following, DataError.Remote> =
        safeCall<Following> {
            httpClient.get("${baseUrl()}/users/$followingId/is-following") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            }
        }

    override suspend fun obtainFollowers(
        userId: String,
        limit: Int,
        offset: Int,
    ): DataResult<List<User>, DataError.Remote> =
        safeCall<List<User>> {
            httpClient.get("${baseUrl()}/users/$userId/followers") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }

    override suspend fun obtainFollowing(
        userId: String,
        limit: Int,
        offset: Int,
    ): DataResult<List<User>, DataError.Remote> =
        safeCall<List<User>> {
            httpClient.get("${baseUrl()}/users/$userId/following") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }

    override suspend fun countFollowers(
        userId: String
    ): DataResult<Count, DataError.Remote> =
        safeCall<Count> {
            httpClient.get("${baseUrl()}/users/$userId/followers/count") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            }
        }

    override suspend fun countFollows(
        userId: String
    ): DataResult<Count, DataError.Remote> =
        safeCall<Count> {
            httpClient.get("${baseUrl()}/users/$userId/following/count") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            }
        }
}
