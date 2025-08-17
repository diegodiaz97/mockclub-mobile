package com.diego.futty.home.design.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.design.data.network.RemoteDiscoveryDataSource
import com.diego.futty.home.design.domain.repository.DiscoverRepository
import com.diego.futty.home.design.presentation.util.SearchType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras

class DiscoverRepositoryImpl(
    private val remoteDiscoveryDataSource: RemoteDiscoveryDataSource
) : DiscoverRepository {
    override suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote> {
        return remoteDiscoveryDataSource.searchUsers(query)
    }

    override suspend fun searchPosts(
        currentUserId: String,
        query: String,
        type: SearchType,
        limit: Int,
        offset: Int
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return remoteDiscoveryDataSource.searchPosts(
            currentUserId = currentUserId,
            query = query,
            type = type,
            limit = limit,
            offset = offset
        )
    }
}
