package com.diego.futty.home.post.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.post.domain.repository.PostRepository
import com.diego.futty.home.view.HomeRoute
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
) : PostViewContract, ViewModel() {

    private val _postMaxLength = mutableStateOf(100)
    override val postMaxLength: State<Int> = _postMaxLength

    private val _text = mutableStateOf("")
    override val text: State<String> = _text

    private val _team = mutableStateOf("a")
    override val team: State<String> = _team

    private val _brand = mutableStateOf("b")
    override val brand: State<String> = _brand

    private val _images = mutableStateOf<List<ByteArray>>(emptyList())
    override val images: State<List<ByteArray>> = _images

    private val _launchGallery = mutableStateOf(false)
    override val launchGallery: State<Boolean> = _launchGallery

    private val _launchCamera = mutableStateOf(false)
    override val launchCamera: State<Boolean> = _launchCamera

    private val _showTags = mutableStateOf(false)
    override val showTags: State<Boolean> = _showTags

    private val _newTags = mutableStateOf<List<String>>(emptyList())
    override val newTags: State<List<String>> = _newTags

    private val _existingTags = mutableStateOf<List<String>>(emptyList())
    override val existingTags: State<List<String>> = _existingTags

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
    }

    override fun createPost(
        onStartPostCreation: () -> Unit,
        onPostCreated: () -> Unit
    ) {
        if (text.value.isEmpty() || team.value.isEmpty() || brand.value.isEmpty()) {
            _modal.value = Modal.GenericErrorModal(
                onPrimaryAction = { _modal.value = null },
                onDismiss = { _modal.value = null },
            )
            return
        }
        onStartPostCreation()
        saveTags()

        viewModelScope.launch {
            postRepository.createPost(
                text = _text.value,
                images = _images.value,
                team = _team.value,
                brand = _brand.value,
                tags = _newTags.value,
            ).onSuccess {
                onPostCreated()
                dismissPostCreation()
            }.onError {
                _modal.value = Modal.GenericErrorModal(
                    onPrimaryAction = { _modal.value = null },
                    onDismiss = { _modal.value = null },
                )
            }
        }
    }

    private fun getTags() {
        viewModelScope.launch {
            postRepository.getAllTags()
                .onSuccess {tags ->
                    _existingTags.value = tags.map { it.name }
                }.onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    private fun saveTags() {
        viewModelScope.launch {
            postRepository.createOrUpdateTags(newTags.value)
                .onSuccess {
                    _existingTags.value = _existingTags.value.plus(_newTags.value).distinct()
                }.onError {
                    _modal.value = Modal.GenericErrorModal(
                        onPrimaryAction = { _modal.value = null },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun updateText(newText: String) {
        if (newText.length <= _postMaxLength.value) {
            _text.value = newText
        }
    }

    override fun updateTeam(newTeam: String) {
        _team.value = newTeam
    }

    override fun updateBrand(newBrand: String) {
        _brand.value = newBrand
    }

    override fun launchGallery() {
        _launchGallery.value = _launchGallery.value.not()
    }

    override fun launchCamera() {
        _launchCamera.value = _launchCamera.value.not()
    }

    override fun dismissPostCreation() {
        _text.value = ""
        _team.value = "a"
        _brand.value = "b"
        _images.value = emptyList()
        _newTags.value = emptyList()
        _showTags.value = false
    }

    override fun onImagesSelected(images: List<ByteArray>) {
        _images.value = images
    }

    override fun onRemoveImageSelected(index: Int) {
        _images.value = _images.value.filterIndexed { i, _ -> i != index }
    }

    override fun showTags() {
        if (_existingTags.value.isEmpty()) {
            getTags()
        }
        _showTags.value = _showTags.value.not()
    }

    override fun addTags(tags: List<String>) {
        _newTags.value = tags
        _showTags.value = _showTags.value.not()
    }

    override fun removeTag(index: Int) {
        _newTags.value = _newTags.value.filterIndexed { i, _ -> i != index }
    }
}
