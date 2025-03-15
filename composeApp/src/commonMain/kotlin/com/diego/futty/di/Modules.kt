package com.diego.futty.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.diego.futty.bookdeprecated.data.database.DatabaseFactory
import com.diego.futty.bookdeprecated.data.database.FavoriteBookDatabase
import com.diego.futty.bookdeprecated.data.network.KtorRemoteBookDataSource
import com.diego.futty.bookdeprecated.data.network.RemoteBookDataSource
import com.diego.futty.bookdeprecated.data.repository.DefaultBookRepository
import com.diego.futty.bookdeprecated.domain.BookRepository
import com.diego.futty.bookdeprecated.presentation.SelectedBookViewModel
import com.diego.futty.bookdeprecated.presentation.book_detail.BookDetailViewModel
import com.diego.futty.bookdeprecated.presentation.book_list.BookListViewModel
import com.diego.futty.core.data.HttpClientFactory
import com.diego.futty.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.match.data.network.KtorRemoteLiveScoresDataSource
import com.diego.futty.match.data.network.RemoteLiveScoresDataSource
import com.diego.futty.match.data.repository.LiveScoresRepositoryImpl
import com.diego.futty.match.domain.LiveScoresRepository
import com.diego.futty.match.presentation.viewmodel.MatchViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()
    singleOf(::KtorRemoteLiveScoresDataSource).bind<RemoteLiveScoresDataSource>()
    singleOf(::LiveScoresRepositoryImpl).bind<LiveScoresRepository>()

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::DesignViewModel)
    viewModelOf(::MatchViewModel)
    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}
