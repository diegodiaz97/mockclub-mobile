package com.diego.futty.home.post.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.post.domain.repository.PostRepository
import com.diego.futty.home.view.HomeRoute
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
    private val preferences: UserPreferences,
) : PostViewContract, ViewModel() {

    private val _text = mutableStateOf("")
    override val text: State<String> = _text

    private val _team = mutableStateOf("")
    override val team: State<String> = _team

    private val _brand = mutableStateOf("")
    override val brand: State<String> = _brand

    private val _images = mutableStateOf<List<ByteArray>>(emptyList())
    override val images: State<List<ByteArray>> = _images

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
    }

    override fun createPost() {
        viewModelScope.launch {
            postRepository.createPost(
                text = _text.value,
                images = images.value,
                team = _team.value,
                brand = _brand.value,
            )
                .onSuccess {
                    _navigate(HomeRoute.Feed)
                }
                .onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }
}
