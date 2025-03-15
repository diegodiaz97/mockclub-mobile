package com.diego.futty.bookdeprecated.presentation.book_list

import com.diego.futty.bookdeprecated.domain.Book
import com.diego.futty.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)
