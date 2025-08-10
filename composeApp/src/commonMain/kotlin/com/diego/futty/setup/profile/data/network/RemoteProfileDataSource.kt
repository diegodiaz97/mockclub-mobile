package com.diego.futty.setup.profile.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.Count
import com.diego.futty.home.feed.domain.model.Following
import com.diego.futty.home.feed.domain.model.User

interface RemoteProfileDataSource {
    suspend fun followUser(targetUserId: String): DataResult<Unit, DataError.Remote>

    suspend fun unfollowUser(targetUserId: String): DataResult<Unit, DataError.Remote>

    suspend fun areYouFollowing(
        followerId: String,
        followingId: String
    ): DataResult<Following, DataError.Remote>

    suspend fun obtainFollowers(
        userId: String,
        limit: Int,
        offset: Int
    ): DataResult<List<User>, DataError.Remote>

    suspend fun obtainFollowing(
        userId: String,
        limit: Int,
        offset: Int
    ): DataResult<List<User>, DataError.Remote>

    suspend fun countFollowers(userId: String): DataResult<Count, DataError.Remote>

    suspend fun countFollows(userId: String): DataResult<Count, DataError.Remote>
}
