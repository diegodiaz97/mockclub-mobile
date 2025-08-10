package com.diego.futty.setup.profile.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.Count
import com.diego.futty.home.feed.domain.model.Following
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.setup.profile.data.network.RemoteProfileDataSource
import com.diego.futty.setup.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remoteProfileDataSource: RemoteProfileDataSource
) : ProfileRepository {

    override suspend fun followUser(targetUserId: String): DataResult<Unit, DataError.Remote> {
        return remoteProfileDataSource.followUser(targetUserId)
    }

    override suspend fun unfollowUser(targetUserId: String): DataResult<Unit, DataError.Remote> {
        return remoteProfileDataSource.unfollowUser(targetUserId)
    }

    override suspend fun areYouFollowing(
        followerId: String,
        followingId: String
    ): DataResult<Following, DataError.Remote> {
        return remoteProfileDataSource.areYouFollowing(followerId, followingId)
    }

    override suspend fun obtainFollowers(
        userId: String,
        limit: Int,
        offset: Int,
    ): DataResult<List<User>, DataError.Remote> {
        return remoteProfileDataSource.obtainFollowers(userId, limit, offset)
    }

    override suspend fun obtainFollowing(
        userId: String,
        limit: Int,
        offset: Int,
    ): DataResult<List<User>, DataError.Remote> {
        return remoteProfileDataSource.obtainFollowing(userId, limit, offset)
    }


    override suspend fun countFollowers(userId: String): DataResult<Count, DataError.Remote> {
        return remoteProfileDataSource.countFollowers(userId)
    }

    override suspend fun countFollows(userId: String): DataResult<Count, DataError.Remote> {
        return remoteProfileDataSource.countFollows(userId)
    }
}
