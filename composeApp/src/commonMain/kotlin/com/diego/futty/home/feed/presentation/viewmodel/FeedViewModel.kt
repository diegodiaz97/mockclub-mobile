package com.diego.futty.home.feed.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.diego.futty.core.presentation.theme.ErrorLight
import com.diego.futty.core.presentation.theme.InfoLight
import com.diego.futty.core.presentation.theme.toHex
import com.diego.futty.home.feed.domain.model.ActionableImage
import com.diego.futty.home.feed.domain.model.Post
import com.diego.futty.home.feed.domain.model.ProfileImage
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.view.HomeRoute
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import futty.composeapp.generated.resources.compose_multiplatform
import futty.composeapp.generated.resources.dybala
import futty.composeapp.generated.resources.girasoles

class FeedViewModel : FeedViewContract, ViewModel() {
    private val getImages = listOf(
        ActionableImage(image = Res.drawable.dybala) { onImageClicked(it) },
        ActionableImage(image = Res.drawable.girasoles) { onImageClicked(it) },
        ActionableImage(image = Res.drawable.compose_multiplatform) { onImageClicked(it) },
        ActionableImage(image = Res.drawable.book_error_2) { onImageClicked(it) },
    )

    private val getUsers = listOf(
        User(
            id = "",
            email = "",
            name = "Diego Díaz",
            description = "",
            profileImage = ProfileImage(initials = "DD", background = InfoLight.toHex()),
            creationDate = "",
            followers = null,
            following = null,
            level = 3,
            country = "Argentina",
            desires = null,
        ),
        User(
            id = "",
            email = "",
            name = "Carolina Fubel",
            description = "",
            profileImage = ProfileImage(initials = "CF", background = ErrorLight.toHex()),
            creationDate = "",
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
            profileImage = ProfileImage(image = Res.drawable.dybala),
            creationDate = "",
            followers = null,
            following = null,
            level = 3,
            country = "Argentina",
            desires = null,
        )
    )

    private val getPosts = listOf(
        Post(
            id = "",
            user = getUsers[0],
            date = "25 de enero a las 12:32",
            text = "Estoy aprendiendo Jetpack Compose \uD83D\uDEAC \uD83D\uDE0E",
            images = getImages,
            onClick = { },
        ),
        Post(
            id = "",
            user = getUsers[0],
            date = "22 de enero a las 14:43",
            text = "Fotonnnn",
            images = getImages.subList(0,1),
            onClick = {},
        ),
        Post(
            id = "",
            user = getUsers[1],
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
            user = getUsers[2],
            date = "18 de enero a las 18:12",
            text = "Ojalá Paredes le haga un gol a Brasil \uD83C\uDDE6\uD83C\uDDF7 ⚔\uFE0F \uD83C\uDDE7\uD83C\uDDF7",
            onClick = {},
        )
    )

    private val _posts = mutableStateOf<List<Post>?>(null)
    override val posts: State<List<Post>?> = _posts

    private val _openedPost = mutableStateOf<Post?>(null)
    override val openedPost: State<Post?> = _openedPost

    private val _openedImage = mutableStateOf<ActionableImage?>(null)
    override val openedImage: State<ActionableImage?> = _openedImage

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
        _posts.value = getPosts
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
}
