package com.diego.futty.authentication.profileCreation.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

interface RemoteProfileCreationDataSource {
    suspend fun createProfile(user: User): DataResult<String, DataError.Remote>
    suspend fun fetchProfile(id: String): DataResult<User, DataError.Remote>
    suspend fun updateProfile(
        id: String,
        updatedUser: User,
    ): DataResult<String, DataError.Remote>

    suspend fun updateProfileImage(image: ByteArray): DataResult<String, DataError.Remote>
}
