package com.diego.futty.authentication.welcome.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.authentication.welcome.domain.repository.WelcomeRepository
import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val welcomeRepository: WelcomeRepository,
    private val loginRepository: LoginRepository,
) : WelcomeViewContract, ViewModel() {

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _navigate: (AuthenticationRoute) -> Unit = {}
    private var _onLogin: () -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
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
                    _onLogin()
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
                    _onLogin()
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
}
