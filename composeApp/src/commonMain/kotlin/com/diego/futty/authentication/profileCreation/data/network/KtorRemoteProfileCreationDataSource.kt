package com.diego.futty.authentication.profileCreation.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.firebase.ImageUploader
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.data.remote.safeCall
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.feed.domain.model.ProfileImage
import com.diego.futty.home.feed.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class KtorRemoteProfileCreationDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
    private val httpClient: HttpClient,
) : RemoteProfileCreationDataSource {

    private fun baseUrl(): String {
        return if (PlatformInfo.isIOS) {
            //"http://192.168.0.192:8080"
            "http://192.168.0.10:8080" //Casa de mama
        } else {
            "http://10.0.2.2:8080"
        }
    }

    private suspend fun authToken(): String {
        val currentUser = firebaseManager.auth.currentUser
        return currentUser?.getIdToken(false)
            ?: throw IllegalStateException("No token")
    }

    override suspend fun createProfile(user: User): DataResult<String, DataError.Remote> =
        safeCall<String> {
            httpClient.post(
                urlString = "${baseUrl()}/users"
            ) {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
                contentType(ContentType.Application.Json)
                setBody(user)
            }
        }

    override suspend fun fetchProfile(id: String): DataResult<User, DataError.Remote> =
        safeCall<User> {
            httpClient.get(
                urlString = "${baseUrl()}/users/$id"
            ) {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            }
        }

    override suspend fun updateProfile(
        id: String,
        updatedUser: User,
    ): DataResult<String, DataError.Remote> =
        safeCall<String> {
            httpClient.put(
                urlString = "${baseUrl()}/users/$id"
            ) {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
                contentType(ContentType.Application.Json)
                setBody(updatedUser)
            }
        }

    override suspend fun updateProfileImage(image: ByteArray): DataResult<String, DataError.Remote> =
        safeCall {
            val userId = preferences.getUserId() ?: throw IllegalStateException("No user id")
            val imagePath = "profile_images/$userId.jpg"
            val url = ImageUploader().uploadImage(image, imagePath)

            httpClient.put("${baseUrl()}/users/$userId/photo") {
                header(HttpHeaders.Authorization, "Bearer ${authToken()}")
                contentType(ContentType.Application.Json)
                setBody(ProfileImage(image = url, initials = null, background = null))
            }
        }
}
