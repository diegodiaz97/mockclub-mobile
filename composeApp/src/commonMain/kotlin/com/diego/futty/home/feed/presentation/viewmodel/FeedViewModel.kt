package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.authentication.profileCreation.domain.repository.ProfileCreationRepository
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.ErrorLight
import com.diego.futty.core.presentation.theme.toHex
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_BASIC
import com.diego.futty.home.feed.domain.model.ActionableImage
import com.diego.futty.home.feed.domain.model.Post
import com.diego.futty.home.feed.domain.model.ProfileImage
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.view.HomeRoute
import kotlinx.coroutines.launch

class FeedViewModel(
    private val profileCreationRepository: ProfileCreationRepository,
    private val preferences: UserPreferences,
) : FeedViewContract, ViewModel() {
    private val getImages = listOf(
        ActionableImage(image = "https://media.vogue.mx/photos/5cdef9dc1f70818ac67f4c53/2:3/w_2240,c_limit/fotografias%20de%20paisaje.jpg") { onImageClicked(it) },
        ActionableImage(image = "https://media.vogue.mx/photos/5cdef7991f7081b6577f4c49/master/w_1600,c_limit/fotografia%20de%20paisaje%202.jpg") { onImageClicked(it) },
        ActionableImage(image = "https://fotos.perfil.com/2025/02/20/trim/720/410/6j6M-000-36y93mz.jpg?webp") { onImageClicked(it) },
    )

    private val _user = mutableStateOf<User?>(null)
    override val user: State<User?> = _user

    private val _posts = mutableStateOf<List<Post>?>(null)
    override val posts: State<List<Post>?> = _posts

    private val _openedPost = mutableStateOf<Post?>(null)
    override val openedPost: State<Post?> = _openedPost

    private val _openedImage = mutableStateOf<ActionableImage?>(null)
    override val openedImage: State<ActionableImage?> = _openedImage

    private var _navigate: (HomeRoute) -> Unit = {}

    private val getUsers = listOf(
        User(
            id = "",
            email = "",
            name = "Carolina",
            lastName = "Fubel",
            description = "",
            profileImage = ProfileImage(initials = "CF", background = ErrorLight.toHex()),
            creationDate = "",
            userType = USER_TYPE_BASIC,
            followers = null,
            following = null,
            level = 3,
            country = "Argentina",
            desires = null,
        ),
        User(
            id = "",
            email = "",
            name = "Bizzarap",
            description = "",
            profileImage = ProfileImage(
                image = "https://forbes.es/wp-content/uploads/2022/08/Forbes_ListaCreativos_Web_Bizarrap.jpg",
                background = "0xFF28A745"
            ),
            creationDate = "",
            userType = USER_TYPE_BASIC,
            followers = null,
            following = null,
            level = 3,
            country = "Argentina",
            desires = null,
        )
    )

    private fun getPosts(user: User) = listOf(
        Post(
            id = "",
            user = user,
            date = "25 de enero a las 12:32",
            text = "Estoy aprendiendo Jetpack Compose \uD83D\uDEAC \uD83D\uDE0E",
            images = getImages,
            onClick = { },
        ),
        Post(
            id = "",
            user = user,
            date = "22 de enero a las 14:43",
            text = "Fotonnnn",
            images = getImages.subList(0,1),
            onClick = {},
        ),
        Post(
            id = "",
            user = getUsers[0],
            date = "23 de enero a las 18:43",
            text = "Hoy vi unos girasoles muy amarilloss",
            images = getImages.subList(1,2),
            onClick = {},
        ),
        Post(
            id = "",
            user = getUsers[0],
            date = "22 de enero a las 13:21",
            text = "Que ganas de comer helado",
            onClick = {},
        ),
        Post(
            id = "",
            user = getUsers[1],
            date = "18 de enero a las 18:12",
            text = "Ojalá Paredes le haga un gol a Brasil \uD83C\uDDE6\uD83C\uDDF7 ⚔\uFE0F \uD83C\uDDE7\uD83C\uDDF7",
            onClick = {},
        )
    )

    fun setup(navController: NavHostController) {
        fetchUserInfo()
        _navigate = { navController.navigate(it) }
    }

    override fun onProfileClicked() {
        _navigate(HomeRoute.Setup)
    }

    override fun onImageClicked(actionableImage: ActionableImage) {
        _openedImage.value = actionableImage
    }

    override fun onImageClosed() {
        _openedImage.value = null
    }

    override fun onPostClicked(post: Post) {
        _openedPost.value = post
    }

    override fun onPostClosed() {
        _openedPost.value = null
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = preferences.getUserId() ?: ""
            profileCreationRepository.fetchProfile(user)
                .onSuccess { loggedUser ->
                    // show info
                    _user.value = loggedUser
                    _posts.value = getPosts(loggedUser)
                    preferences.saveUserType(loggedUser.userType)
                }
                .onError {
                    // show error
                }
        }
    }
}
