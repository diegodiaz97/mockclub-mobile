package com.diego.futty.home.design.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

interface RemoteDiscoveryDataSource {
    suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote>
}