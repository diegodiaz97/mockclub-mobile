package com.diego.futty.home.design.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User

class KtorRemoteDiscoveryDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemoteDiscoveryDataSource {
    override suspend fun searchUsers(query: String): DataResult<List<User>, DataError.Remote> {
        return try {
            val firestore = firebaseManager.firestore
            val usersCollection = firestore.collection("users")

            // Búsqueda por nombre
            val nameQuery = usersCollection
                .orderBy("nameLowercase")
                .startAt(query)
                .endAt(query + "\uf8ff") // Agrega el carácter especial para rango
                .get().documents.map { it.data<User>() }
                .filter { it.id != preferences.getUserId() }

            // Búsqueda por nombre
            val lastnameQuery = usersCollection
                .orderBy("lastNameLowercase")
                .startAt(query)
                .endAt(query + "\uf8ff") // Agrega el carácter especial para rango
                .get().documents.map { it.data<User>() }
                .filter { it.id != preferences.getUserId() }

            val result = (nameQuery + lastnameQuery).distinctBy { it.id }

            return DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}
