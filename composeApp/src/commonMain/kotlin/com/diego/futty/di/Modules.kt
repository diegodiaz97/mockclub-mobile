package com.diego.futty.di

import com.diego.futty.authentication.login.data.network.KtorRemoteLoginDataSource
import com.diego.futty.authentication.login.data.network.RemoteLoginDataSource
import com.diego.futty.authentication.login.data.repository.LoginRepositoryImpl
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.authentication.login.presentation.viewmodel.LoginViewModel
import com.diego.futty.authentication.profileCreation.data.network.KtorRemoteProfileCreationDataSource
import com.diego.futty.authentication.profileCreation.data.network.RemoteProfileCreationDataSource
import com.diego.futty.authentication.profileCreation.data.repository.ProfileCreationRepositoryImpl
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.authentication.profileCreation.presentation.viewmodel.ProfileCreationViewModel
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
import com.diego.futty.home.design.data.network.KtorRemoteDiscoveryDataSource
import com.diego.futty.home.design.data.network.RemoteDiscoveryDataSource
import com.diego.futty.home.design.data.repository.DiscoverRepositoryImpl
import com.diego.futty.home.design.domain.repository.DiscoverRepository
import com.diego.futty.home.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import com.diego.futty.home.match.data.network.KtorRemoteLiveScoresDataSource
import com.diego.futty.home.match.data.network.RemoteLiveScoresDataSource
import com.diego.futty.home.match.data.repository.LiveScoresRepositoryImpl
import com.diego.futty.home.match.domain.LiveScoresRepository
import com.diego.futty.home.match.presentation.viewmodel.MatchViewModel
import com.diego.futty.home.post.data.network.KtorRemotePostDataSource
import com.diego.futty.home.post.data.network.RemotePostDataSource
import com.diego.futty.home.post.data.repository.PostRepositoryImpl
import com.diego.futty.home.post.domain.repository.PostRepository
import com.diego.futty.home.view.HomeViewModel
import com.diego.futty.setup.profile.data.network.KtorRemoteProfileDataSource
import com.diego.futty.setup.profile.data.network.RemoteProfileDataSource
import com.diego.futty.setup.profile.data.repository.ProfileRepositoryImpl
import com.diego.futty.setup.profile.domain.repository.ProfileRepository
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import com.diego.futty.setup.settings.data.network.KtorRemoteLogoutDataSource
import com.diego.futty.setup.settings.data.network.RemoteLogoutDataSource
import com.diego.futty.setup.settings.data.repository.SettingsRepositoryImpl
import com.diego.futty.setup.settings.domain.repository.SettingsRepository
import com.diego.futty.setup.settings.presentation.viewmodel.SettingsViewModel
import com.diego.futty.setup.view.SetupViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // Deprecated
    singleOf(::KtorRemoteLiveScoresDataSource).bind<RemoteLiveScoresDataSource>()
    singleOf(::LiveScoresRepositoryImpl).bind<LiveScoresRepository>()
    viewModelOf(::MatchViewModel)

    // Authentication
    single { HttpClientFactory.create(get()) }
    single { FirebaseManager }
    single { provideSettings() }
    single { UserPreferences(get()) }

    singleOf(::KtorRemoteWelcomeDataSource).bind<RemoteWelcomeDataSource>()
    singleOf(::WelcomeRepositoryImpl).bind<WelcomeRepository>()

    singleOf(::KtorRemoteSignupDataSource).bind<RemoteSignupDataSource>()
    singleOf(::SignupRepositoryImpl).bind<SignupRepository>()

    singleOf(::KtorRemoteProfileCreationDataSource).bind<RemoteProfileCreationDataSource>()
    singleOf(::ProfileCreationRepositoryImpl).bind<ProfileCreationRepository>()

    singleOf(::KtorRemoteLoginDataSource).bind<RemoteLoginDataSource>()
    singleOf(::LoginRepositoryImpl).bind<LoginRepository>()

    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::ProfileCreationViewModel)
    viewModelOf(::LoginViewModel)

    // Home
    singleOf(::KtorRemoteDiscoveryDataSource).bind<RemoteDiscoveryDataSource>()
    singleOf(::DiscoverRepositoryImpl).bind<DiscoverRepository>()

    viewModelOf(::HomeViewModel)
    viewModelOf(::DesignViewModel)
    viewModelOf(::FeedViewModel)

    // Post
    singleOf(::KtorRemotePostDataSource).bind<RemotePostDataSource>()
    singleOf(::PostRepositoryImpl).bind<PostRepository>()
    //viewModelOf(::HomeViewModel)

    // Profile
    singleOf(::KtorRemoteProfileDataSource).bind<RemoteProfileDataSource>()
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()

    viewModelOf(::ProfileViewModel)

    // Setup
    singleOf(::KtorRemoteLogoutDataSource).bind<RemoteLogoutDataSource>()
    singleOf(::SettingsRepositoryImpl).bind<SettingsRepository>()

    viewModelOf(::SetupViewModel)
    viewModelOf(::SettingsViewModel)
}
