package com.diego.futty.authentication.signup.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.signup.domain.repository.SignupRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.design.presentation.component.banner.Banner
import com.diego.futty.design.presentation.component.banner.BannerStatus
import com.diego.futty.design.utils.RegexUtils
import kotlinx.coroutines.launch

class SignupViewModel(
    private val signupRepository: SignupRepository
) : SignupViewContract, ViewModel() {

    private val _email = mutableStateOf("")
    override val email: State<String> = _email

    private val _password = mutableStateOf("")
    override val password: State<String> = _password

    private val _confirmPassword = mutableStateOf("")
    override val confirmPassword: State<String> = _confirmPassword

    private val _banner = mutableStateOf<Banner?>(null)
    override val banner: State<Banner?> = _banner

    private val _canCreateAccount = mutableStateOf(false)
    override val canCreateAccount: State<Boolean> = _canCreateAccount

    private val _hideKeyboard = mutableStateOf(true)
    override val hideKeyboard: State<Boolean> = _hideKeyboard

    private var _navigate: (AuthenticationRoute) -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
        _back = { navController.popBackStack() }
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

    override fun updateConfirmPassword(password: String) {
        _confirmPassword.value = password
        validateButtonEnabled()
    }

    override fun onSignupClicked() {
        if (isValidEmail().not()) {
            _banner.value = Banner.StatusBanner(
                title = "Email incorrecto",
                subtitle = "Por favor verifica que tenga un formato válido.",
                status = BannerStatus.Error
            )
            return
        }
        if (isValidPassword().not()) {
            _banner.value = Banner.StatusBanner(
                title = "Contraseña incorrecta",
                subtitle = "Por favor verifica que tenga un formato válido.",
                status = BannerStatus.Error
            )
            return
        }
        _banner.value = null
        signUpWithMail()
    }

    override fun onBackClicked() {
        _back()
    }

    private fun signUpWithMail() {
        viewModelScope.launch {
            signupRepository.signUpWithEmail(_email.value, _password.value)
                .onSuccess {
                    _banner.value = Banner.StatusBanner(
                        title = "¡Listo!",
                        subtitle = it,
                        status = BannerStatus.Success
                    )
                }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        title = "Algo salió mal",
                        subtitle = "No pudimos crear el usuario. (${it.name})",
                        status = BannerStatus.Error
                    )
                }
        }
    }

    private fun validateButtonEnabled() {
        _banner.value = null
        _canCreateAccount.value =
            _email.value.isNotEmpty() && _password.value.isNotEmpty() && _confirmPassword.value.isNotEmpty()
    }

    private fun isValidPassword() =
        RegexUtils.passwordRegex.matches(_password.value) && _password.value == _confirmPassword.value

    private fun isValidEmail() = RegexUtils.emailRegex.matches(_email.value)
}
