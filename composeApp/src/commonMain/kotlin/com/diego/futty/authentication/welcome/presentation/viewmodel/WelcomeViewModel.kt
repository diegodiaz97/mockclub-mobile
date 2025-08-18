package com.diego.futty.authentication.welcome.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.authentication.welcome.domain.repository.WelcomeRepository
import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.getRandomLightColorHex
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_BASIC
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.ProfileImage
import com.diego.futty.home.feed.domain.model.User
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class WelcomeViewModel(
    private val welcomeRepository: WelcomeRepository,
    private val loginRepository: LoginRepository,
    private val profileCreationRepository: ProfileCreationRepository,
    private val preferences: UserPreferences,
) : WelcomeViewContract, ViewModel() {

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _navigate: (AuthenticationRoute) -> Unit = {}
    private var _onLogin: () -> Unit = {}
    private var _onSignUp: () -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
        _onSignUp = {
            navController.navigate(AuthenticationRoute.ProfileCreation) {
                popUpTo(AuthenticationRoute.Signup) { inclusive = true }
            }
        }
        _onLogin = {
            navController.navigate(AuthenticationRoute.Home) {
                popUpTo(AuthenticationRoute.Login) { inclusive = true }
            }
        }
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            welcomeRepository.checkUserSession()
                .onSuccess { authState ->
                    when (authState) {
                        AuthState.LoggedOut -> {}
                        AuthState.LoggedIn -> {
                            _navigate(AuthenticationRoute.Home)
                            welcomeRepository.initializeServerTime()
                        }
                    }
                }
        }
    }

    override fun onLoginWithGoogleClicked() {
        viewModelScope.launch {
            loginRepository.loginWithGoogle()
                .onSuccess {
                    fetchUserInfo()
                }
                .onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun onLoginWithAppleClicked() {
        viewModelScope.launch {
            loginRepository.loginWithApple()
                .onSuccess {
                    fetchUserInfo()
                }
                .onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun onLoginWithEmailClicked() {
        _navigate(AuthenticationRoute.Login)
    }

    override fun onSignupClicked() {
        _navigate(AuthenticationRoute.Signup)
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = preferences.getUserId() ?: return@launch
            profileCreationRepository.fetchProfile(user)
                .onSuccess {
                    _onLogin()
                }
                .onError {
                    createUser()
                }
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            val user = User(
                id = preferences.getUserId() ?: "",
                email = preferences.getUserEmail() ?: "",
                username = preferences.getUserEmail() ?: "",
                creationDate = Clock.System.now().toEpochMilliseconds(),
                userType = USER_TYPE_BASIC,
                name = null,
                lastName = null,
                description = null,
                profileImage = ProfileImage(
                    background = getRandomLightColorHex(),
                    initials = null,
                    image = null,
                ),
                country = null,
            )

            profileCreationRepository.createProfile(user)
                .onSuccess {
                    _onSignUp()
                }
                .onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }
}
