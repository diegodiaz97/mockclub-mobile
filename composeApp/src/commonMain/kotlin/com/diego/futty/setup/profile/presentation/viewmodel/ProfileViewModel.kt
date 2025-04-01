package com.diego.futty.setup.profile.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.AlertLight
import com.diego.futty.core.presentation.theme.ErrorLight
import com.diego.futty.core.presentation.theme.InfoLight
import com.diego.futty.core.presentation.theme.SuccessLight
import com.diego.futty.home.design.presentation.component.Chip.ChipModel
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.setup.view.SetupRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.BallFootball
import compose.icons.tablericons.BrandPinterest
import compose.icons.tablericons.Headphones
import compose.icons.tablericons.Message2
import compose.icons.tablericons.Palette
import compose.icons.tablericons.Pizza
import compose.icons.tablericons.Plane
import compose.icons.tablericons.School
import compose.icons.tablericons.Video
import compose.icons.tablericons.Wallet
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileCreationRepository: ProfileCreationRepository,
    private val preferences: UserPreferences,
) : ProfileViewContract, ViewModel() {

    private val dummyChips = listOf(
        ChipModel(
            icon = TablerIcons.School,
            color = AlertLight,
            text = "Cursos"
        ),
        ChipModel(
            icon = TablerIcons.Pizza,
            color = InfoLight,
            text = "Comidas"
        ),
        ChipModel(
            icon = TablerIcons.Video,
            color = SuccessLight,
            text = "Cine"
        ),
        ChipModel(
            icon = TablerIcons.Palette,
            color = ErrorLight,
            text = "Arte"
        ),
        ChipModel(
            icon = TablerIcons.Message2,
            color = AlertLight,
            text = "Idiomas"
        ),
        ChipModel(
            icon = TablerIcons.Headphones,
            color = InfoLight,
            text = "Música"
        ),
        ChipModel(
            icon = TablerIcons.BrandPinterest,
            color = SuccessLight,
            text = "Decoración"
        ),
        ChipModel(
            icon = TablerIcons.Wallet,
            color = ErrorLight,
            text = "Finanzas"
        ),
        ChipModel(
            icon = TablerIcons.BallFootball,
            color = AlertLight,
            text = "Deportes"
        ),
        ChipModel(
            icon = TablerIcons.Plane,
            color = InfoLight,
            text = "Vacaciones"
        ),
    )

    private val _user = mutableStateOf<User?>(null)
    override val user: State<User?> = _user

    private val _showUpdateImage = mutableStateOf(false)
    override val showUpdateImage: State<Boolean> = _showUpdateImage

    private val _urlImage = mutableStateOf<String?>(null)
    override val urlImage: State<String?> = _urlImage

    private val _initials = mutableStateOf<String?>(null)
    override val initials: State<String?> = _initials

    private val _chipItems = mutableStateOf<List<ChipModel>?>(null)
    override val chipItems: State<List<ChipModel>?> = _chipItems

    private val _selectedChips = mutableStateOf<List<ChipModel>>(emptyList())
    override val selectedChips: State<List<ChipModel>> = _selectedChips

    private val _bitmapImage = mutableStateOf<ImageBitmap?>(null)
    private val _byteArrayImage = mutableStateOf<ByteArray?>(null)
    private var _navigate: (SetupRoute) -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(
        navController: NavHostController,
        onBack: () -> Unit,
    ) {
        fetchUserInfo()
        _chipItems.value = dummyChips
        _navigate = { navController.navigate(it) }
        _back = onBack
    }

    override fun onSettingsClicked() {
        _navigate(SetupRoute.Setting)
    }

    override fun onBackClicked() {
        _back()
    }

    override fun onChipSelected(chip: ChipModel) {
        if (_selectedChips.value.contains(chip)) {
            _selectedChips.value -= chip
        } else {
            _selectedChips.value += chip
        }
    }

    override fun updateImage(imageBitmap: ImageBitmap, imageByteArray: ByteArray) {
        _urlImage.value = null
        _initials.value = null
        _bitmapImage.value = imageBitmap
        _byteArrayImage.value = imageByteArray
        updateProfileImage()
    }

    override fun showUpdateImage(show: Boolean) {
        _showUpdateImage.value = show
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = preferences.getUserId() ?: ""
            profileCreationRepository.fetchProfile(user)
                .onSuccess { loggedUser ->
                    // show info
                    _user.value = loggedUser
                    _urlImage.value = loggedUser.profileImage?.image
                    _initials.value = loggedUser.profileImage?.initials
                }
                .onError {
                    // show error
                }
        }
    }

    private fun updateProfileImage() {
        viewModelScope.launch {
            profileCreationRepository.updateProfileImage(_byteArrayImage.value!!)
                .onSuccess { url ->
                    _urlImage.value = url
                }
                .onError {
                    _initials.value = user.value?.profileImage?.initials
                    // show error
                }
        }
    }
}
