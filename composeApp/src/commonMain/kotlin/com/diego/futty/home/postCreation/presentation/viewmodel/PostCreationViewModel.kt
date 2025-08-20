package com.diego.futty.home.postCreation.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.design.presentation.component.image.AspectRatio
import com.diego.futty.home.postCreation.domain.repository.PostRepository
import com.diego.futty.home.postCreation.presentation.component.IdentityRequest
import com.skydoves.flexible.core.InternalFlexibleApi
import com.skydoves.flexible.core.log
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.launch

class PostCreationViewModel(
    private val postRepository: PostRepository,
) : PostCreationViewContract, ViewModel() {

    private val _postMaxLength = mutableStateOf(200)
    override val postMaxLength: State<Int> = _postMaxLength

    private val _text = mutableStateOf("")
    override val text: State<String> = _text

    private val _team = mutableStateOf("")
    override val team: State<String> = _team

    private val _brand = mutableStateOf("")
    override val brand: State<String> = _brand

    private val _teamLogo = mutableStateOf<ByteArray?>(null)
    override val teamLogo: State<ByteArray?> = _teamLogo

    private val _brandLogo = mutableStateOf<ByteArray?>(null)
    override val brandLogo: State<ByteArray?> = _brandLogo

    private val _designerLogo = mutableStateOf<ByteArray?>(null)
    override val designerLogo: State<ByteArray?> = _designerLogo

    private val _images = mutableStateOf<List<ByteArray>>(emptyList())
    override val images: State<List<ByteArray>> = _images

    private val _imageRatio = mutableStateOf(AspectRatio.Square)
    override val imageRatio: State<AspectRatio> = _imageRatio

    private val _launchGallery = mutableStateOf(false)
    override val launchGallery: State<Boolean> = _launchGallery

    private val _showIdentity = mutableStateOf(false)
    override val showIdentity: State<Boolean> = _showIdentity

    private val _showTags = mutableStateOf(false)
    override val showTags: State<Boolean> = _showTags

    private val _newTags = mutableStateOf<List<String>>(emptyList())
    override val newTags: State<List<String>> = _newTags

    private val _existingTags = mutableStateOf<List<String>>(emptyList())
    override val existingTags: State<List<String>> = _existingTags

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    override fun createPost(
        onStartPostCreation: () -> Unit,
        onPostCreated: () -> Unit
    ) {
        val teamLogo = _teamLogo.value
        val brandLogo = _brandLogo.value
        val designerLogo = _designerLogo.value

        if (text.value.isEmpty() || team.value.isEmpty() || brand.value.isEmpty() || teamLogo == null || brandLogo == null) {
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
                ratio = _imageRatio.value.ratio,
                team = _team.value,
                brand = _brand.value,
                teamLogo = teamLogo,
                brandLogo = brandLogo,
                designerLogo = designerLogo,
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

    override fun updateRatio() {
        _imageRatio.value = if (_imageRatio.value == AspectRatio.Square) {
            AspectRatio.Portrait
        } else {
            AspectRatio.Square
        }
    }

    override fun launchGallery() {
        _launchGallery.value = _launchGallery.value.not()
    }

    override fun dismissPostCreation() {
        _text.value = ""
        _team.value = ""
        _brand.value = ""
        _images.value = emptyList()
        _newTags.value = emptyList()
        _teamLogo.value = null
        _brandLogo.value = null
        _designerLogo.value = null
        _showIdentity.value = false
        _showTags.value = false
    }

    @OptIn(InternalFlexibleApi::class)
    override fun onImagesSelected(images: List<PlatformFile>) {
        viewModelScope.launch {
            try {
                val imagesBitmap = images.map { it.toImageBitmap() }
                val imagesByteArray = imagesBitmap.map { it.encodeToByteArray(
                    format = ImageFormat.PNG, // JPEG or PNG
                    quality = 60 // Compression quality (0-100)
                ) }
                _images.value = imagesByteArray
            } catch (e: Exception) {
                log(e.message ?: "Error al subir imagen de post")
            }
        }
    }

    override fun onRemoveImageSelected(index: Int) {
        _images.value = _images.value.filterIndexed { i, _ -> i != index }
    }

    override fun showIdentity() {
        _showIdentity.value = _showIdentity.value.not()
    }

    override fun updateIdentity(newIdentity: IdentityRequest) {
        _team.value = newIdentity.teamName
        _brand.value = newIdentity.brandName
        _teamLogo.value = newIdentity.teamLogo
        _brandLogo.value = newIdentity.brandLogo
        _designerLogo.value = newIdentity.designerLogo
        _showIdentity.value = _showIdentity.value.not()
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
