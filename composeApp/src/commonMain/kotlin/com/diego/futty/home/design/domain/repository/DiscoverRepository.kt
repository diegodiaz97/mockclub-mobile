package com.diego.futty.home.design.domain.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

interface DiscoverRepository {
    suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote>
}
