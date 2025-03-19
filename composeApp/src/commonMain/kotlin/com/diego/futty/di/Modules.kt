package com.diego.futty.di

import com.diego.futty.app.presentation.viewmodel.HomeViewModel
import com.diego.futty.authentication.login.data.network.KtorRemoteLoginDataSource
import com.diego.futty.authentication.login.data.network.RemoteLoginDataSource
import com.diego.futty.authentication.login.data.repository.LoginRepositoryImpl
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.authentication.login.presentation.viewmodel.LoginViewModel
import com.diego.futty.authentication.signup.data.network.KtorRemoteSignupDataSource
import com.diego.futty.authentication.signup.data.network.RemoteSignupDataSource
import com.diego.futty.authentication.signup.data.repository.SignupRepositoryImpl
import com.diego.futty.authentication.signup.domain.repository.SignupRepository
import com.diego.futty.authentication.signup.presentation.viewmodel.SignupViewModel
import com.diego.futty.authentication.view.AuthenticationViewModel
import com.diego.futty.authentication.welcome.data.network.KtorRemoteWelcomeDataSource
import com.diego.futty.authentication.welcome.data.network.RemoteWelcomeDataSource
import com.diego.futty.authentication.welcome.data.repository.WelcomeRepositoryImpl
import com.diego.futty.authentication.welcome.domain.repository.WelcomeRepository
import com.diego.futty.authentication.welcome.presentation.viewmodel.WelcomeViewModel
import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.data.local.provideSettings
import com.diego.futty.core.data.remote.HttpClientFactory
import com.diego.futty.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.match.data.network.KtorRemoteLiveScoresDataSource
import com.diego.futty.match.data.network.RemoteLiveScoresDataSource
import com.diego.futty.match.data.repository.LiveScoresRepositoryImpl
import com.diego.futty.match.domain.LiveScoresRepository
import com.diego.futty.match.presentation.viewmodel.MatchViewModel
import com.diego.futty.profile.presentation.viewmodel.ProfileViewModel
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

    // Authentication
    single { FirebaseManager }
    single { provideSettings() }
    single { UserPreferences(get()) }

    singleOf(::KtorRemoteWelcomeDataSource).bind<RemoteWelcomeDataSource>()
    singleOf(::WelcomeRepositoryImpl).bind<WelcomeRepository>()

    singleOf(::KtorRemoteSignupDataSource).bind<RemoteSignupDataSource>()
    singleOf(::SignupRepositoryImpl).bind<SignupRepository>()

    singleOf(::KtorRemoteLoginDataSource).bind<RemoteLoginDataSource>()
    singleOf(::LoginRepositoryImpl).bind<LoginRepository>()

    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::LoginViewModel)

    // Home
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
}
