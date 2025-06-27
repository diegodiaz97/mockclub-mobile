package com.diego.futty.setup.profile.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.setup.profile.data.network.RemoteProfileDataSource
import com.diego.futty.setup.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remoteProfileDataSource: RemoteProfileDataSource
) : ProfileRepository {

    override suspend fun followUser(
        followerId: String,
        followingId: String
    ): DataResult<String, DataError.Remote> {
        return remoteProfileDataSource.followUser(followerId, followingId)
    }

    override suspend fun unfollowUser(
        followerId: String,
        followingId: String
    ): DataResult<String, DataError.Remote> {
        return remoteProfileDataSource.unfollowUser(followerId, followingId)
    }

    override suspend fun areYouFollowing(
        followerId: String,
        followingId: String
    ): DataResult<Boolean, DataError.Remote> {
        return remoteProfileDataSource.areYouFollowing(followerId, followingId)
    }

    override suspend fun obtainFollowers(userId: String): DataResult<List<String>, DataError.Remote> {
        return remoteProfileDataSource.obtainFollowers(userId)
    }

    override suspend fun obtainFollows(userId: String): DataResult<List<String>, DataError.Remote> {
        return remoteProfileDataSource.obtainFollows(userId)
    }


    override suspend fun countFollowers(userId: String): DataResult<Int, DataError.Remote> {
        return remoteProfileDataSource.countFollowers(userId)
    }

    override suspend fun countFollows(userId: String): DataResult<Int, DataError.Remote> {
        return remoteProfileDataSource.countFollows(userId)
    }
}
