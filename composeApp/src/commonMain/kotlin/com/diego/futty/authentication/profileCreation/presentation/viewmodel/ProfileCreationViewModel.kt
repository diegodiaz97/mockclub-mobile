package com.diego.futty.authentication.profileCreation.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.authentication.view.AuthenticationRoute
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.feed.domain.model.User
import kotlinx.coroutines.launch

class ProfileCreationViewModel(
    private val profileCreationRepository: ProfileCreationRepository,
    private val preferences: UserPreferences,
) : ProfileCreationViewContract, ViewModel() {

    private val _banner = mutableStateOf<Banner?>(null)
    override val banner: State<Banner?> = _banner

    private val _background = mutableStateOf("0xFF71D88A")
    override val background: State<String> = _background

    private val _name = mutableStateOf("")
    override val name: State<String> = _name

    private val _lastName = mutableStateOf("")
    override val lastName: State<String> = _lastName

    private val _initials = mutableStateOf<String?>(null)
    override val initials: State<String?> = _initials

    private val _description = mutableStateOf("")
    override val description: State<String> = _description

    private val _showUpdateImage = mutableStateOf(false)
    override val showUpdateImage: State<Boolean> = _showUpdateImage

    private val _launchGallery = mutableStateOf(false)
    override val launchGallery: State<Boolean> = _launchGallery

    private val _launchCamera = mutableStateOf(false)
    override val launchCamera: State<Boolean> = _launchCamera

    private val _urlImage = mutableStateOf<String?>(null)
    override val urlImage: State<String?> = _urlImage

    private val _country = mutableStateOf("")
    override val country: State<String> = _country

    private val _canContinue = mutableStateOf(true)
    override val canContinue: State<Boolean> = _canContinue

    private val _hideKeyboard = mutableStateOf(true)
    override val hideKeyboard: State<Boolean> = _hideKeyboard

    private val _user = mutableStateOf<User?>(null)
    private val _bitmapImage = mutableStateOf<ImageBitmap?>(null)
    private val _byteArrayImage = mutableStateOf<ByteArray?>(null)
    private var _onClose: () -> Unit = {}

    fun setup(navController: NavHostController, flow: String? = "home") {
        fetchUserInfo()
        _onClose = {
            when (flow) {
                "home" -> {
                    navController.navigate(AuthenticationRoute.Home) {
                        popUpTo(AuthenticationRoute.ProfileCreation) { inclusive = true }
                    }
                }

                else -> navController.popBackStack()
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

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = preferences.getUserId() ?: ""
            profileCreationRepository.fetchProfile(user)
                .onSuccess { loggedUser ->
                    // show info
                    _user.value = loggedUser
                    _name.value = loggedUser.name ?: ""
                    _lastName.value = loggedUser.lastName ?: ""
                    _description.value = loggedUser.description ?: ""
                    _urlImage.value = loggedUser.profileImage?.image
                    _initials.value = loggedUser.profileImage?.initials
                    _background.value = loggedUser.profileImage?.background ?: ""
                    _country.value = loggedUser.country ?: ""
                }
                .onError {
                    // show error
                }
        }
    }

    override fun onContinueClicked() {
        viewModelScope.launch {
            val id = preferences.getUserId() ?: ""

            val updates: MutableMap<String, String> = mutableMapOf()

            if (_name.value.isNotEmpty()) {
                updates["name"] = _name.value
                updates["lastName"] = _lastName.value
                updates["profileImage.initials"] = _initials.value ?: ""
            }
            if (_description.value.isNotEmpty()) {
                updates["description"] = _description.value
            }

            if (_country.value.isNotEmpty()) {
                updates["country"] = _country.value
            }

            profileCreationRepository.updateProfile(id, updates)
                .onSuccess {
                    _onClose()
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
        _onClose()
    }

    override fun updateImage(imageBitmap: ImageBitmap, imageByteArray: ByteArray) {
        _urlImage.value = null
        _initials.value = null
        _bitmapImage.value = imageBitmap
        _byteArrayImage.value = imageByteArray
        updateProfileImage()
    }

    private fun updateProfileImage() {
        viewModelScope.launch {
            profileCreationRepository.updateProfileImage(_byteArrayImage.value!!)
                .onSuccess { url ->
                    _urlImage.value = url
                }
                .onError {
                    _initials.value = _user.value?.profileImage?.initials
                    // show error
                }
        }
    }

    override fun showUpdateImage() {
        _showUpdateImage.value = _showUpdateImage.value.not()
    }

    override fun launchGallery() {
        _launchGallery.value = _launchGallery.value.not()
    }

    override fun launchCamera() {
        _launchCamera.value = _launchCamera.value.not()
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
