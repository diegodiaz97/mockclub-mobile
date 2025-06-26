package com.diego.futty.home.design.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.home.design.data.repository.DiscoverRepositoryImpl
import com.diego.futty.home.design.presentation.component.bottomsheet.Modal
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.view.HomeRoute
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class DesignViewModel(
    private val discoverRepository: DiscoverRepositoryImpl,
) : DesignViewContract, ViewModel() {
    private val _searchText = MutableStateFlow("")
    override val searchText: StateFlow<String> = _searchText

    private val _searchUsers = MutableStateFlow<List<User>>(emptyList())
    override val searchUsers: StateFlow<List<User>> = _searchUsers

    private val _clickedUser = mutableStateOf("")
    override val clickedUser: State<String> = _clickedUser

    private val _modal = mutableStateOf<Modal?>(null)
    override val modal: State<Modal?> = _modal

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        debounce()
        _navigate = { navController.navigate(it) }
        _modal.value = Modal.GenericModal(
            image = "https://framerusercontent.com/images/nrDI4qjkcIIXHS1wDcmvfuW9Q.png",
            title = "Busca personas",
            subtitle = "Aquí puedes encontrar a tus amigos o nuevo contenido.",
            primaryButton = "Entendido",
            secondaryButton = null,
            onPrimaryAction = { _modal.value = null },
            onSecondaryAction = { },
            onDismiss = { _modal.value = null },
        )
    }

    override fun onProfileClicked() {
        _navigate(HomeRoute.Setup)
    }

    @OptIn(FlowPreview::class)
    private fun debounce() {
        viewModelScope.launch {
            _searchText.debounce(300) // Espera 300ms después de la última pulsación
                .filter { it.isNotEmpty() } // No busca si el texto está vacío
                .collectLatest { query ->
                    searchUsers(query)
                }
        }
    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            discoverRepository.searchUsers(query)
                .onSuccess { users ->
                    _searchUsers.value = users
                }
                .onError {
                    _modal.value = Modal.GenericModal(
                        image = "https://cdn3d.iconscout.com/3d/free/thumb/free-warning-3d-illustration-download-in-png-blend-fbx-gltf-file-formats--alert-error-danger-sign-user-interface-pack-illustrations-4715732.png",
                        title = "Algo salió mal",
                        subtitle = "Intenta de nuevo más tarde.",
                        primaryButton = "Entendido",
                        secondaryButton = null,
                        onPrimaryAction = { _modal.value = null },
                        onSecondaryAction = { },
                        onDismiss = { _modal.value = null },
                    )
                }
        }
    }

    override fun updateSearch(search: String) {
        _searchText.value = search
        if (search.isEmpty()) {
            _searchUsers.value = emptyList()
        }
    }

    override fun onUserClicked(user: User) {
        _clickedUser.value = user.id
        _navigate(HomeRoute.Setup)
    }

    override fun resetUserId() {
        _clickedUser.value = ""
    }
}
