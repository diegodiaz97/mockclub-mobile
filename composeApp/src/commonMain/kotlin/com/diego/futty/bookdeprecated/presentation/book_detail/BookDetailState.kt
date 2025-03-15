package com.diego.futty.bookdeprecated.presentation.book_detail

import com.diego.futty.bookdeprecated.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
