package com.diego.futty.core.presentation.photos

fun firebaseUrlToWorkerUrl(firebaseUrl: String): String? {
    // Verificamos que sea una URL válida de Firebase Storage
    if (!firebaseUrl.contains("firebasestorage.googleapis.com")) return firebaseUrl

    // Buscamos el inicio del path codificado
    val pathStart = firebaseUrl.indexOf("/o/")
    if (pathStart == -1) return null

    val pathAndQuery = firebaseUrl.substring(pathStart + 3) // después de "/o/"
    val parts = pathAndQuery.split("?")

    val encodedPath = parts.getOrNull(0) ?: return null // Ej: profile_images%2Fnombre.jpg
    val query = parts.getOrNull(1) ?: return null

    // Decodeamos "%2F" manualmente (Firebase solo usa ese encoding para "/")
    val decodedPath = encodedPath.replace("%2F", "/")

    // Extraemos el token de la query
    val tokenParam = query.split("&").firstOrNull { it.startsWith("token=") } ?: return null
    val token = tokenParam.removePrefix("token=")

    return "https://hidden-hill-89d9.dieggodiaz97.workers.dev/$decodedPath?token=$token"
}
