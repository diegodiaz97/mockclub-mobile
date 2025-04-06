package com.diego.futty.home.design.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.design.data.network.RemoteDiscoveryDataSource
import com.diego.futty.home.design.domain.repository.DiscoverRepository
import com.diego.futty.home.feed.domain.model.User

class DiscoverRepositoryImpl(
    private val remoteDiscoveryDataSource: RemoteDiscoveryDataSource
) : DiscoverRepository {
    override suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote> {
        return remoteDiscoveryDataSource.searchUsers(query)
    }
}
