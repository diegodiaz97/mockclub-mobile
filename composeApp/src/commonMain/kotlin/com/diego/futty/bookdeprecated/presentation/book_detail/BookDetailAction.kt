package com.diego.futty.bookdeprecated.presentation.book_detail

import com.diego.futty.bookdeprecated.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavoriteClick: BookDetailAction
    data class OnSelectedBookChange(val book: Book): BookDetailAction
}