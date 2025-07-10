package com.diego.futty.home.postCreation.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.design.presentation.component.image.AspectRatio

interface PostCreationViewContract {
    val text: State<String>
    val team: State<String>
    val brand: State<String>
    val images: State<List<ByteArray>>
    val imageRatio: State<AspectRatio>
    val launchGallery: State<Boolean>
    val postMaxLength: State<Int>
    val showTags: State<Boolean>
    val existingTags: State<List<String>>
    val newTags: State<List<String>>
    val modal: State<Modal?>
    fun updateText(newText: String)
    fun updateTeam(newTeam: String)
    fun updateBrand(newBrand: String)
    fun updateRatio()
    fun launchGallery()
    fun dismissPostCreation()
    fun createPost(onStartPostCreation: () -> Unit, onPostCreated: () -> Unit)
    fun onImagesSelected(images: List<ByteArray>)
    fun onRemoveImageSelected(index: Int)
    fun showTags()
    fun addTags(tags: List<String>)
    fun removeTag(index: Int)
}
