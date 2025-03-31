package com.diego.futty.authentication.profileCreation.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.getRandomLightColorHex
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import kotlinx.coroutines.launch

class ProfileCreationViewModel(
    private val profileCreationRepository: ProfileCreationRepository,
    private val preferences: UserPreferences,
) : ProfileCreationViewContract, ViewModel() {

    private val _banner = mutableStateOf<Banner?>(null)
    override val banner: State<Banner?> = _banner

    private val _background = mutableStateOf("")
    override val background: State<String> = _background

    private val _name = mutableStateOf("")
    override val name: State<String> = _name

    private val _lastName = mutableStateOf("")
    override val lastName: State<String> = _lastName

    private val _initials = mutableStateOf("")
    override val initials: State<String> = _initials

    private val _description = mutableStateOf("")
    override val description: State<String> = _description

    private val _image = mutableStateOf("")
    override val image: State<String> = _image

    private val _country = mutableStateOf("")
    override val country: State<String> = _country

    private val _canContinue = mutableStateOf(true)
    override val canContinue: State<Boolean> = _canContinue

    private val _hideKeyboard = mutableStateOf(true)
    override val hideKeyboard: State<Boolean> = _hideKeyboard

    private var _onLogin: () -> Unit = {}

    fun setup(navController: NavHostController) {
        _background.value = getRandomLightColorHex()
        _onLogin = {
            navController.navigate(AuthenticationRoute.Home) {
                popUpTo(AuthenticationRoute.ProfileCreation) { inclusive = true }
            }
        }
    }

    override fun hideKeyboard() {
        _hideKeyboard.value = _hideKeyboard.value.not()
    }

    override fun updateName(name: String) {
        _name.value = name
        validateButtonEnabled()
    }

    override fun updateLastName(lastName: String) {
        _lastName.value = lastName
        validateButtonEnabled()
    }

    override fun updateDescription(description: String) {
        _description.value = description
    }

    override fun updateCountry(country: String) {
        _country.value = country
        validateButtonEnabled()
    }

    override fun updateProfileImage(imageUri: String) {
        _image.value = imageUri
    }

    override fun onContinueClicked() {
        viewModelScope.launch {
            val id = preferences.getUserId() ?: ""

            val updates: MutableMap<String, String> = mutableMapOf()

            if (_name.value.isNotEmpty()) {
                updates["name"] = _name.value
                updates["lastName"] = _lastName.value
                updates["profileImage.background"] = _background.value
                updates["profileImage.initials"] = _initials.value
            }
            if (_description.value.isNotEmpty()) {
                updates["description"] = _description.value
            }
            if (_image.value.isNotEmpty()) {
                updates["profileImage.image"] = _image.value
            }
            if (_country.value.isNotEmpty()) {
                updates["country"] = _country.value
            }

            profileCreationRepository.updateProfile(id, updates)
                .onSuccess {
                    _onLogin()
                }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        title = "Algo salió mal",
                        subtitle = "No pudimos actualizar el usuario en Firestore. (${it.name})",
                        status = BannerStatus.Error
                    )
                }
        }
    }

    override fun onCloseClicked() {
        _onLogin()
    }

    private fun validateButtonEnabled() {
        updateInitials()
        _banner.value = null
        _canContinue.value =
            _name.value.isNotEmpty() && _lastName.value.isNotEmpty() && _country.value.isNotEmpty()
    }

    private fun updateInitials() {
        when {
            _name.value.isNotEmpty() && _lastName.value.isNotEmpty() -> {
                _initials.value = _name.value.substring(0, 1) + _lastName.value.substring(0, 1)
            }

            _name.value.isNotEmpty() -> {
                _initials.value = _name.value.substring(0, 1)
            }

            _lastName.value.isNotEmpty() -> {
                _initials.value = _lastName.value.substring(0, 1)
            }

            else -> {
                _initials.value = ""
            }
        }
    }
}
