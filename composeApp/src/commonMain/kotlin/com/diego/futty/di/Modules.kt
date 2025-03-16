package com.diego.futty.di

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
    singleOf(::KtorRemoteLiveScoresDataSource).bind<RemoteLiveScoresDataSource>()
    singleOf(::LiveScoresRepositoryImpl).bind<LiveScoresRepository>()

    viewModelOf(::DesignViewModel)
    viewModelOf(::MatchViewModel)
}
