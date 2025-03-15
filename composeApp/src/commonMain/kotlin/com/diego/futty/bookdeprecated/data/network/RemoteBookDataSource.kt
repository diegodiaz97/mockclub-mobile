package com.diego.futty.bookdeprecated.data.network

import com.diego.futty.bookdeprecated.data.dto.BookWorkDto
import com.diego.futty.bookdeprecated.data.dto.SearchResponseDto
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote>
}