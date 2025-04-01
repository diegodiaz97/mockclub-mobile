package com.diego.futty.authentication.profileCreation.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.ImageBitmap
import com.diego.futty.home.design.presentation.component.banner.Banner

interface ProfileCreationViewContract {
    val banner: State<Banner?>
    val background: State<String>
    val name: State<String>
    val lastName: State<String>
    val description: State<String>
    val showUpdateImage: State<Boolean>
    val launchGallery: State<Boolean>
    val launchCamera: State<Boolean>
    val urlImage: State<String?>
    val initials: State<String?>
    val country: State<String>
    val canContinue: State<Boolean>
    val hideKeyboard: State<Boolean>
    fun updateName(name: String)
    fun updateLastName(lastName: String)
    fun updateDescription(description: String)
    fun updateImage(imageBitmap: ImageBitmap, imageByteArray: ByteArray)
    fun showUpdateImage()
    fun launchGallery()
    fun launchCamera()
    fun updateCountry(country: String)
    fun onContinueClicked()
    fun onCloseClicked()
    fun hideKeyboard()
}
