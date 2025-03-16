package com.diego.futty.design.data

import com.diego.futty.core.data.FirebaseManager

class FirebaseAuthRepository {
   /* suspend fun loginUser(email: String, password: String): String? {
        return try {
            val user = FirebaseManager.auth.signInWithEmailAndPassword(email, password)
            user.user?.uid // Retorna el ID del usuario si todo sale bien
        } catch (e: Exception) {
            println("Error al iniciar sesión: ${e.message}")
            null
        }
    }

    suspend fun saveUserData(userId: String, name: String, age: Int) {
        FirebaseManager.firestore.collection("users")
            .document(userId)
            .set(mapOf("name" to name, "age" to age))
    }

    suspend fun uploadFile(userId: String, fileData: ByteArray) {
        val ref = FirebaseManager.storage.reference("uploads/$userId/profile.jpg")
        //ref.putBytes(fileData)
    }*/
}
