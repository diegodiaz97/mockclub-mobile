package com.diego.futty.authentication.login.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.login.domain.repository.LoginRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.utils.RegexUtils
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
) : LoginViewContract, ViewModel() {
    private val _email = mutableStateOf("")
    override val email: State<String> = _email

    private val _password = mutableStateOf("")
    override val password: State<String> = _password

    private val _banner = mutableStateOf<Banner?>(null)
    override val banner: State<Banner?> = _banner

    private val _canLogin = mutableStateOf(false)
    override val canLogin: State<Boolean> = _canLogin

    private val _hideKeyboard = mutableStateOf(true)
    override val hideKeyboard: State<Boolean> = _hideKeyboard

    private var _navigate: (AuthenticationRoute) -> Unit = {}
    private var _onLogin: () -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
        _onLogin = {
            navController.navigate(AuthenticationRoute.Home) {
                popUpTo(AuthenticationRoute.Login) { inclusive = true }
            }
        }
    }

    override fun hideKeyboard() {
        _hideKeyboard.value = _hideKeyboard.value.not()
    }

    override fun updateEmail(email: String) {
        _email.value = email
        validateButtonEnabled()
    }

    override fun updatePassword(password: String) {
        _password.value = password
        validateButtonEnabled()
    }

    override fun onLoginClicked() {
        if (isValidEmail().not()) {
            _banner.value = Banner.StatusBanner(
                BannerUIData(
                    title = "Email incorrecto",
                    description = "Por favor verifica que tenga un formato válido.",
                    status = BannerStatus.Error
                )
            )
            return
        }
        if (isValidPassword().not()) {
            _banner.value = Banner.StatusBanner(
                BannerUIData(
                    title = "Contraseña incorrecta",
                    description = "Por favor verifica que tenga un formato válido.",
                    status = BannerStatus.Error
                )
            )
            return
        }
        _banner.value = null
        loginWithMail()
    }

    override fun onSignupClicked() {
        _navigate(AuthenticationRoute.Signup)
    }

    override fun onRecoveryClicked() {
        TODO("Not yet implemented")
    }

    override fun onLoginWithGoogleClicked() {
        TODO("Not yet implemented")
    }

    override fun onLoginWithAppleClicked() {
        TODO("Not yet implemented")
    }

    override fun onLoginWithBiometricsClicked() {
        TODO("Not yet implemented")
    }

    private fun loginWithMail() {
        viewModelScope.launch {
            loginRepository.loginWithEmail(_email.value, _password.value)
                .onSuccess {
                    _banner.value = Banner.StatusBanner(
                        BannerUIData(
                            title = "¡Listo!",
                            description = "Inicio de sesión exitosos",
                            status = BannerStatus.Success
                        )
                    )
                    _onLogin()
                }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        BannerUIData(
                            title = "Algo salió mal",
                            description = "No se pudo iniciar sesión.",
                            status = BannerStatus.Error
                        )
                    )
                }
        }
    }

    private fun validateButtonEnabled() {
        _banner.value = null
        _canLogin.value = _email.value.isNotEmpty() && _password.value.isNotEmpty()
    }

    private fun isValidPassword() = RegexUtils.passwordRegex.matches(_password.value)

    private fun isValidEmail() = RegexUtils.emailRegex.matches(_email.value)
}
