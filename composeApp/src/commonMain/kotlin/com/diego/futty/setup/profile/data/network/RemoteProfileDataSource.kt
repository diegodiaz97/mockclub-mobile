package com.diego.futty.setup.profile.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult

interface RemoteProfileDataSource {
    suspend fun followUser(
        followerId: String,
        followingId: String
    ): DataResult<String, DataError.Remote>

    suspend fun unfollowUser(
        followerId: String,
        followingId: String
    ): DataResult<String, DataError.Remote>

    suspend fun areYouFollowing(
        followerId: String,
        followingId: String
    ): DataResult<Boolean, DataError.Remote>

    suspend fun obtainFollowers(userId: String): DataResult<List<String>, DataError.Remote>

    suspend fun obtainFollows(userId: String): DataResult<List<String>, DataError.Remote>

    suspend fun countFollowers(userId: String): DataResult<Int, DataError.Remote>

    suspend fun countFollows(userId: String): DataResult<Int, DataError.Remote>
}
