package com.diego.futty.authentication.signup.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.banner.Banner

interface SignupViewContract {
    val email: State<String>
    val userName: State<String>
    val password: State<String>
    val confirmPassword: State<String>
    val banner: State<Banner?>
    val canCreateAccount: State<Boolean>
    fun updateEmail(email: String)
    fun updateUsername(username: String)
    fun updatePassword(password: String)
    fun updateConfirmPassword(password: String)
    fun onSignupClicked()
    fun onBackClicked()
}
