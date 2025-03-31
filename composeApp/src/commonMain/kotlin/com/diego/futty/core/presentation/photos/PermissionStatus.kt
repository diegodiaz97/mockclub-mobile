package com.diego.futty.core.presentation.photos

enum class PermissionStatus {
    GRANTED,   // Permiso concedido
    DENIED,    // Permiso denegado
    SHOW_RATIONAL, // Permiso denegado pero el usuario debe ser informado
    NEVER_ASK  // El usuario ha seleccionado "No preguntar nuevamente"
}
