package com.diego.futty.authentication.welcome.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal

interface WelcomeViewContract {
    val modal: State<Modal?>
    fun onLoginWithGoogleClicked()
    fun onLoginWithAppleClicked()
    fun onLoginWithEmailClicked()
    fun onSignupClicked()
}