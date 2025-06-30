package com.diego.futty.home.post.presentation.viewmodel

import androidx.compose.runtime.State
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal

interface PostViewContract {
    val text: State<String>
    val images: State<List<ByteArray>>
    val brand: State<String>
    val team: State<String>
    val modal: State<Modal?>
    fun createPost()
}
