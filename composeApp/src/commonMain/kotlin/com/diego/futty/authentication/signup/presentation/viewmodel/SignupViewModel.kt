package com.diego.futty.authentication.signup.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.authentication.signup.domain.repository.SignupRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.getRandomLightColorHex
import com.diego.futty.core.presentation.utils.RegexUtils
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_BASIC
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.feed.domain.model.ProfileImage
import com.diego.futty.home.feed.domain.model.User
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class SignupViewModel(
    private val signupRepository: SignupRepository,
    private val profileCreationRepository: ProfileCreationRepository,
    private val preferences: UserPreferences,
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

    private var _navigate: () -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = {
            navController.navigate(AuthenticationRoute.ProfileCreation) {
                popUpTo(AuthenticationRoute.Signup) { inclusive = true }
            }
        }
        _back = { navController.popBackStack() }
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
        signUpWithMail()
    }

    override fun onBackClicked() {
        _back()
    }

    private fun signUpWithMail() {
        viewModelScope.launch {
            signupRepository.signUpWithEmail(_email.value, _password.value)
                .onSuccess {
                    createUser()
                }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        BannerUIData(
                            title = "Algo salió mal",
                            description = "No pudimos authenticar el usuario en FirebaseAuth. (${it.name})",
                            status = BannerStatus.Error
                        )
                    )
                }
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            val user = User(
                id = preferences.getUserId() ?: "",
                email = preferences.getUserEmail() ?: "",
                profileImage = ProfileImage(
                    background = getRandomLightColorHex()
                ),
                creationDate = Clock.System.now().toEpochMilliseconds(),
                userType = USER_TYPE_BASIC,
                level = 0,
            )

            profileCreationRepository.createProfile(user)
                .onSuccess {
                    _navigate()
                }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        BannerUIData(
                            title = "Algo salió mal",
                            description = "No pudimos crear el usuario en Firestore. (${it.name})",
                            status = BannerStatus.Error
                        )
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
