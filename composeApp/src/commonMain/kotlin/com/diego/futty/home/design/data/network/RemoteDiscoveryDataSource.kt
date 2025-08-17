package com.diego.futty.home.design.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.design.presentation.util.SearchType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.PostWithExtras

interface RemoteDiscoveryDataSource {
    suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote>
    suspend fun searchPosts(
        currentUserId: String,
        query: String,
        type: SearchType,
        limit: Int = 10,
        offset: Int = 0
    ): DataResult<List<PostWithExtras>, DataError.Remote>
}