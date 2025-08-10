package com.diego.futty.authentication.profileCreation.data.repository

import com.diego.futty.authentication.profileCreation.data.network.RemoteProfileCreationDataSource
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

class ProfileCreationRepositoryImpl(
    private val remoteProfileCreationDataSource: RemoteProfileCreationDataSource
) : ProfileCreationRepository {

    override suspend fun createProfile(user: User): DataResult<String, DataError.Remote> {
        return remoteProfileCreationDataSource.createProfile(user)
    }

    override suspend fun fetchProfile(id: String): DataResult<User, DataError.Remote> {
        return remoteProfileCreationDataSource.fetchProfile(id)
    }

    override suspend fun updateProfile(
        id: String,
        updatedUser: User,
    ): DataResult<String, DataError.Remote> {
        return remoteProfileCreationDataSource.updateProfile(id, updatedUser)
    }

    override suspend fun updateProfileImage(image: ByteArray): DataResult<String, DataError.Remote> {
        return remoteProfileCreationDataSource.updateProfileImage(image)
    }
}
