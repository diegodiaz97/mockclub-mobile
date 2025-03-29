package com.diego.futty.authentication.profileCreation.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.banner.Banner

interface ProfileCreationViewContract {
    val banner: State<Banner?>
    val background: State<String>
    val name: State<String>
    val lastName: State<String>
    val description: State<String>
    val image: State<String>
    val initials: State<String>
    val country: State<String>
    val canContinue: State<Boolean>
    val hideKeyboard: State<Boolean>
    fun updateName(name: String)
    fun updateLastName(lastName: String)
    fun updateDescription(description: String)
    fun updateProfileImage(imageUri: String)
    fun updateCountry(country: String)
    fun onContinueClicked()
    fun onCloseClicked()
    fun hideKeyboard()
}
