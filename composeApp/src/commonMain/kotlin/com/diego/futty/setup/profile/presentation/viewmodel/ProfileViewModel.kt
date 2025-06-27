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
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.design.presentation.component.chip.ChipModel
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.setup.profile.domain.repository.ProfileRepository
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
    private val profileRepository: ProfileRepository,
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

    private val _userId = mutableStateOf("")
    override val userId: State<String> = _userId

    private val _showUpdateImage = mutableStateOf(false)
    override val showUpdateImage: State<Boolean> = _showUpdateImage

    private val _launchGallery = mutableStateOf(false)
    override val launchGallery: State<Boolean> = _launchGallery

    private val _launchCamera = mutableStateOf(false)
    override val launchCamera: State<Boolean> = _launchCamera

    private val _urlImage = mutableStateOf<String?>(null)
    override val urlImage: State<String?> = _urlImage

    private val _initials = mutableStateOf<String?>(null)
    override val initials: State<String?> = _initials

    private val _chipItems = mutableStateOf<List<ChipModel>?>(null)
    override val chipItems: State<List<ChipModel>?> = _chipItems

    private val _selectedChips = mutableStateOf<List<ChipModel>>(emptyList())
    override val selectedChips: State<List<ChipModel>> = _selectedChips

    private val _postsCant = mutableStateOf<Int?>(0) // set to default = null when i have real posts
    override val postsCant: State<Int?> = _postsCant

    private val _followersCant = mutableStateOf<Int?>(null)
    override val followersCant: State<Int?> = _followersCant

    private val _followsCant = mutableStateOf<Int?>(null)
    override val followsCant: State<Int?> = _followsCant

    private val _showFollowButton = mutableStateOf(false)
    override val showFollowButton: State<Boolean> = _showFollowButton

    private val _followingUser = mutableStateOf(false)
    override val followingUser: State<Boolean> = _followingUser

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private val _bitmapImage = mutableStateOf<ImageBitmap?>(null)
    private val _byteArrayImage = mutableStateOf<ByteArray?>(null)
    private var _navigate: (SetupRoute) -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(
        userId: String = "",
        navController: NavHostController,
        onBack: () -> Unit,
    ) {
        _userId.value = userId
        _chipItems.value = dummyChips
        _navigate = { navController.navigate(it) }
        _back = onBack
        fetchUserInfo()
        setupFollowers()
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

    override fun showUpdateImage() {
        _showUpdateImage.value = _showUpdateImage.value.not()
    }

    override fun launchGallery() {
        _launchGallery.value = _launchGallery.value.not()
    }

    override fun launchCamera() {
        _launchCamera.value = _launchCamera.value.not()
    }

    override fun onEditClicked() {
        _navigate(SetupRoute.ProfileCreation)
    }

    override fun onVerifyClicked() {
        // Go to upgrade screen
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = _userId.value.ifEmpty { preferences.getUserId() ?: "" }
            profileCreationRepository.fetchProfile(user)
                .onSuccess { loggedUser ->
                    // show info
                    _user.value = loggedUser
                    _urlImage.value = loggedUser.profileImage.image
                    _initials.value = loggedUser.profileImage.initials
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
                    _initials.value = _user.value?.profileImage?.initials
                    // show error
                }
        }
    }

    private fun setupFollowers() {
        val user = _userId.value
        countFollowers()
        countFollows()
        if (user.isNotEmpty()) {
            areYouFollowing(user)
        }
    }

    override fun onFollowOrUnfollowClicked() {
        val otherUser = _userId.value
        if (_followingUser.value) {
            unfollowUser(otherUser)
        } else {
            followUser(otherUser)
        }
    }

    private fun followUser(followingId: String) {
        val user = preferences.getUserId() ?: return
        viewModelScope.launch {
            profileRepository.followUser(followerId = user, followingId = followingId)
                .onSuccess {
                    _followersCant.value = _followersCant.value?.plus(1)
                    _followingUser.value = true
                }
                .onError {
                    _modal.value = Modal.GenericModal(
                        image = "https://cdn3d.iconscout.com/3d/free/thumb/free-warning-3d-illustration-download-in-png-blend-fbx-gltf-file-formats--alert-error-danger-sign-user-interface-pack-illustrations-4715732.png",
                        title = "Algo salió mal",
                        subtitle = it.name,//"Intenta de nuevo más tarde.",
                        primaryButton = "Entendido",
                        secondaryButton = null,
                        onPrimaryAction = { _modal.value = null },
                        onSecondaryAction = { },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    private fun unfollowUser(followingId: String) {
        val user = preferences.getUserId() ?: return
        viewModelScope.launch {
            profileRepository.unfollowUser(followerId = user, followingId = followingId)
                .onSuccess {
                    _followersCant.value = _followersCant.value?.minus(1)
                    _followingUser.value = false
                }
                .onError {
                    _modal.value = Modal.GenericModal(
                        image = "https://cdn3d.iconscout.com/3d/free/thumb/free-warning-3d-illustration-download-in-png-blend-fbx-gltf-file-formats--alert-error-danger-sign-user-interface-pack-illustrations-4715732.png",
                        title = "Algo salió mal",
                        subtitle = it.name,//"Intenta de nuevo más tarde.",
                        primaryButton = "Entendido",
                        secondaryButton = null,
                        onPrimaryAction = { _modal.value = null },
                        onSecondaryAction = { },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    private fun areYouFollowing(followingId: String) {
        val user = preferences.getUserId() ?: return
        viewModelScope.launch {
            profileRepository.areYouFollowing(followerId = user, followingId = followingId)
                .onSuccess { following ->
                    _followingUser.value = following
                    _showFollowButton.value = true
                }
                .onError {
                    // SHOW ERROR
                }
        }
    }

    override fun obtainFollowers() {
        val user = _userId.value.ifEmpty { preferences.getUserId() ?: "" }
        viewModelScope.launch {
            profileRepository.obtainFollowers(userId = user)
                .onSuccess {
                    // _urlImage.value = url
                    // UPDATE FOLLOWING USER PROFILE UI
                }
                .onError {
                    // SHOW ERROR
                }
        }
    }

    override fun obtainFollows() {
        val user = _userId.value.ifEmpty { preferences.getUserId() ?: "" }
        viewModelScope.launch {
            profileRepository.obtainFollows(userId = user)
                .onSuccess {
                    // _urlImage.value = url
                    // UPDATE FOLLOWING USER PROFILE UI
                }
                .onError {
                    // SHOW ERROR
                }
        }
    }

    private fun countFollowers() {
        val user = _userId.value.ifEmpty { preferences.getUserId() ?: "" }
        viewModelScope.launch {
            profileRepository.countFollowers(userId = user)
                .onSuccess {
                    _followersCant.value = it
                }
                .onError {
                    // SHOW ERROR
                }
        }
    }

    private fun countFollows() {
        val user = _userId.value.ifEmpty { preferences.getUserId() ?: "" }
        viewModelScope.launch {
            profileRepository.countFollows(userId = user)
                .onSuccess {
                    _followsCant.value = it
                }
                .onError {
                    // SHOW ERROR
                }
        }
    }
}
