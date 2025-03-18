package com.diego.futty.authentication.welcome.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.authentication.welcome.domain.repository.WelcomeRepository
import com.diego.futty.core.data.firebase.AuthState
import com.diego.futty.core.domain.onSuccess
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val welcomeRepository: WelcomeRepository,
) : WelcomeViewContract, ViewModel() {
    private var _navigate: (AuthenticationRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = {
            navController.navigate(it) {
                popUpTo(AuthenticationRoute.Welcome) { inclusive = true }
            }
        }
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            welcomeRepository.checkUserSession()
                .onSuccess { authState ->
                    when (authState) {
                        AuthState.LoggedOut -> _navigate(AuthenticationRoute.Login)
                        AuthState.LoggedIn -> _navigate(AuthenticationRoute.Home)
                    }
                }
        }
    }
}
