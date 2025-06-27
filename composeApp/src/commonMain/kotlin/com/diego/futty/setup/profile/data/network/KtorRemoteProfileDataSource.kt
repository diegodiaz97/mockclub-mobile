package com.diego.futty.setup.profile.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import dev.gitlive.firebase.firestore.FieldValue
import kotlinx.datetime.Clock

class KtorRemoteProfileDataSource(
    private val firebaseManager: FirebaseManager,
) : RemoteProfileDataSource {

    private val firestore = firebaseManager.firestore
    private val followsCollection = firestore.collection("follows")
    private val usersCollection = firestore.collection("users")

    override suspend fun followUser(
        followerId: String,
        followingId: String
    ): DataResult<String, DataError.Remote> {
        return try {
            val followId = "${followerId}_$followingId"

            val data = mapOf(
                "followerId" to followerId,
                "followingId" to followingId,
                "timestamp" to Clock.System.now(),
            )

            followsCollection.document(followId).set(data)

            usersCollection.document(followerId)
                .update("followingCount" to FieldValue.increment(1))
            usersCollection.document(followingId)
                .update("followersCount" to FieldValue.increment(1))

            DataResult.Success("")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun unfollowUser(
        followerId: String,
        followingId: String
    ): DataResult<String, DataError.Remote> {
        return try {
            val followId = "${followerId}_$followingId"

            followsCollection.document(followId).delete()

            usersCollection.document(followerId)
                .update("followingCount" to FieldValue.increment(-1))
            usersCollection.document(followingId)
                .update("followersCount" to FieldValue.increment(-1))

            DataResult.Success("")
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun areYouFollowing(
        followerId: String,
        followingId: String
    ): DataResult<Boolean, DataError.Remote> {
        return try {
            val followId = "${followerId}_$followingId"

            val result = followsCollection.document(followId).get().exists

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun obtainFollowers( // lista de los que me siguen
        userId: String
    ): DataResult<List<String>, DataError.Remote> {
        return try {
            val result = followsCollection
                .where { "followingId" equalTo userId }
                .get()
                .documents
                .map { it.get<String>("followerId") }

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun obtainFollows( // lista de los que yo sigo
        userId: String
    ): DataResult<List<String>, DataError.Remote> {
        return try {
            val result = followsCollection
                .where { "followerId" equalTo userId }
                .get()
                .documents
                .map { it.get<String>("followingId") }

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun countFollowers( // contar seguidores
        userId: String
    ): DataResult<Int, DataError.Remote> {
        return try {
            val result = followsCollection
                .where { "followingId" equalTo userId }
                .get()
                .documents
                .size

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun countFollows( // contar a los que sigo
        userId: String
    ): DataResult<Int, DataError.Remote> {
        return try {
            val result = followsCollection
                .where { "followerId" equalTo userId }
                .get()
                .documents
                .size

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
}
