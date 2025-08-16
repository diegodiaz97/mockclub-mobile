package com.diego.futty.home.postCreation.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.firebase.ImageUploader
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.data.remote.safeCall
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.core.presentation.utils.PlatformInfo
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.CommentWithExtras
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.postCreation.domain.model.Tag
import com.diego.futty.home.postCreation.domain.request.CreateCommentRequest
import com.diego.futty.home.postCreation.domain.request.CreatePostRequest
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Timestamp
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.datetime.Clock

class KtorRemotePostDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
    private val httpClient: HttpClient,
) : RemotePostDataSource {

    private fun baseUrl(): String {
        return if (PlatformInfo.isIOS) {
            "http://192.168.0.192:8080"
        } else {
            "http://10.0.2.2:8080"
        }
    }

    private suspend fun authToken(): String {
        val currentUser = firebaseManager.auth.currentUser
        return currentUser?.getIdToken(false)
            ?: throw IllegalStateException("No token")
    }

    private val firestore = firebaseManager.firestore
    private val usersCollection = firestore.collection("users")
    private val postsCollection = firestore.collection("posts")
    private val tagsCollection = firestore.collection("tags")

    override suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        ratio: Float,
        team: String,
        brand: String,
        tags: List<String>
    ): DataResult<Unit, DataError.Remote> = safeCall {
        require(images.size <= 10) { "Máximo 10 imágenes por post" }
        val userId = preferences.getUserId() ?: ""

        val imageUrls = if (images.isNotEmpty()) {
            images.mapIndexed { index, image ->
                val imagePath = "post_images/${userId}_${
                    Clock.System.now().toEpochMilliseconds()
                }_$index.jpg"
                ImageUploader().uploadImage(image, imagePath)
            }
        } else {
            emptyList()
        }

        httpClient.post("${baseUrl()}/posts/create") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            contentType(ContentType.Application.Json)
            setBody(
                CreatePostRequest(
                    text = text,
                    brand = brand,
                    team = team,
                    ratio = ratio,
                    images = imageUrls,
                    tags = tags
                )
            )
        }
    }

    override suspend fun countPosts(userId: String): DataResult<Int, DataError.Remote> { /*TODO*/
        return try {
            val result = postsCollection
                .where { "userId" equalTo userId }
                .get()
                .documents
                .size

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getFeed(
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<PostWithExtras>, DataError.Remote> = safeCall {
        httpClient.get("${baseUrl()}/posts/feed") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            parameter("limit", limit)
            cursorCreatedAt?.let {
                parameter("cursorCreatedAt", it)
            }
        }
    }

    /*
    override suspend fun getFeedFromFollowedUsers(limit: Int = 20, startAfterTimestamp: Long? = null): DataResult<List<Post>, DataError.Remote> {
        return try {
        val userId = preferences.getUserId() ?: ""
        val followedIds = firestore.collection("users").document(userId)
            .collection("following").get().documents.map { it.id }

        var query = postsCollection
            .whereIn("userId", followedIds.take(10))
            .orderBy("timestamp", descending = true)
            .limit(limit.toLong())

        if (startAfterTimestamp != null) {
            query = query.startAfter(startAfterTimestamp)
        }

        val post = query.get().documents.map { it.data<Post>() }
            DataResult.Success(post)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
    */

    override suspend fun getPostsByUser(
        userId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<PostWithExtras>, DataError.Remote> = safeCall {
        httpClient.get("${baseUrl()}/posts/feed/$userId") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            parameter("limit", limit)
            cursorCreatedAt?.let {
                parameter("cursorCreatedAt", it)
            }
        }
    }

    override suspend fun getPostsByTeam( /*TODO*/
        team: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return try {
            /*val userId = preferences.getUserId() ?: ""
            var query =
                postsCollection.where { "team" equalTo team }
                    .orderBy("timestamp", Direction.DESCENDING)
                    .limit(limit.toLong())
            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val likedPostIds = getLikedPostIdsForUser(userId)

            val posts = query.get().documents.map { doc ->
                val timestamp = doc.get("timestamp") as? Timestamp
                val timestampMillis = timestamp?.toMilliseconds()?.toLong()
                val postId = doc.get("id") as? String ?: doc.id

                val likedByUser = postId in likedPostIds

                Post(
                    id = doc.get("id") as? String ?: doc.id,
                    userId = doc.get("userId") as? String ?: "",
                    text = doc.get("text") as? String ?: "",
                    ratio = doc.get("ratio") as? Float ?: 1f,
                    createdAt = timestampMillis ?: 0L,
                    team = doc.get("team") as? String ?: "",
                    brand = doc.get("brand") as? String ?: ""
                )
            }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithExtras(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)*/
            DataResult.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getPostsByBrand( /*TODO*/
        brand: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return try {
            /*val userId = preferences.getUserId() ?: ""
            var query =
                postsCollection.where { "brand" equalTo brand }
                    .orderBy("timestamp", Direction.DESCENDING)
                    .limit(limit.toLong())
            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }
            val likedPostIds = getLikedPostIdsForUser(userId)

            val posts = query.get().documents.map { doc ->
                val timestamp = doc.get("timestamp") as? Timestamp
                val timestampMillis = timestamp?.toMilliseconds()?.toLong()
                val postId = doc.get("id") as? String ?: doc.id

                val likedByUser = postId in likedPostIds

                Post(
                    id = doc.get("id") as? String ?: doc.id,
                    userId = doc.get("userId") as? String ?: "",
                    text = doc.get("text") as? String ?: "",
                    images = doc.get("imageUrls") as? List<String> ?: emptyList(),
                    ratio = doc.get("ratio") as? Float ?: 1f,
                    createdAt = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = (doc.get("likesCount") as? Int) ?: 0,
                    commentsCount = (doc.get("commentsCount") as? Int) ?: 0,
                    likedByUser = likedByUser,
                    team = doc.get("team") as? String ?: "",
                    brand = doc.get("brand") as? String ?: ""
                )
            }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithExtras(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)*/
            DataResult.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote> = safeCall {
        httpClient.post("${baseUrl()}/posts/$postId/like") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
        }
    }

    override suspend fun unlikePost(postId: String): DataResult<Unit, DataError.Remote> = safeCall {
        httpClient.delete("${baseUrl()}/posts/$postId/like") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
        }
    }

    override suspend fun addComment(
        postId: String,
        text: String,
        parentCommentId: String?
    ): DataResult<CommentWithExtras, DataError.Remote> = safeCall {
        httpClient.post("${baseUrl()}/comments/create") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            contentType(ContentType.Application.Json)
            setBody(
                CreateCommentRequest(
                    postId = postId,
                    text = text,
                    parentCommentId = parentCommentId
                )
            )
        }
    }

    /*override suspend fun addComment( /*TODO*/
        postId: String,
        text: String
    ): DataResult<CommentWithExtras, DataError.Remote> {
        return try {
            /*val userId = preferences.getUserId() ?: ""
            val commentRef = postsCollection.document(postId).collection("comments").document

            firestore.runTransaction {
                commentRef.set(
                    mapOf(
                        "id" to commentRef.id,
                        "userId" to userId,
                        "text" to text,
                        "likesCount" to 0,
                        "likedByUser" to false,
                        "repliesCount" to 0,
                        "timestamp" to FieldValue.serverTimestamp, //save firebase timestamp
                    )
                )
                postsCollection.document(postId)
                    .update(mapOf("commentsCount" to FieldValue.increment(1)))
            }

            val doc = commentRef.get()
            val timestamp = doc.get("timestamp") as? Timestamp
            val timestampMillis = timestamp?.toMilliseconds()?.toLong()

            val comment = Comment(
                id = doc.get("id") as? String ?: doc.id,
                userId = doc.get("userId") as? String ?: "",
                text = doc.get("text") as? String ?: "",
                timestamp = timestampMillis ?: 0L,
                serverTimestamp = doc.get("timestamp"),
                likesCount = 0,
                likedByUser = false,
                replies = emptyList(),
                repliesCount = 0,
            )

            val user = getUserById(userId)
            val commentsWithUser = CommentWithUser(comment, user)

            DataResult.Success(commentsWithUser)*/
        DataResult.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }*/

    override suspend fun replyToComment( /*TODO*/
        postId: String,
        commentId: String,
        text: String
    ): DataResult<CommentWithExtras, DataError.Remote> {
        return try {
            /*val userId = preferences.getUserId() ?: ""
            val replyRef = postsCollection.document(postId)
                .collection("comments").document(commentId)
                .collection("replies").document

            firestore.runTransaction {
                replyRef.set(
                    mapOf(
                        "id" to replyRef.id,
                        "userId" to userId,
                        "text" to text,
                        "likesCount" to 0,
                        "likedByUser" to false,
                        "repliesCount" to 0,
                        "timestamp" to FieldValue.serverTimestamp, //save firebase timestamp
                    )
                )
                postsCollection.document(postId)
                    .collection("comments").document(commentId)
                    .update(mapOf("repliesCount" to FieldValue.increment(1)))
            }

            val doc = replyRef.get()
            val timestamp = doc.get("timestamp") as? Timestamp
            val timestampMillis = timestamp?.toMilliseconds()?.toLong()

            val reply = Comment(
                id = replyRef.id,
                userId = userId,
                text = text,
                timestamp = timestampMillis ?: 0L,
                serverTimestamp = doc.get("timestamp"),
                likesCount = 0,
                likedByUser = false,
                replies = emptyList(),
                repliesCount = 0,
            )

            val user = getUserById(userId)
            val replyWithUser = CommentWithUser(reply, user)

            DataResult.Success(replyWithUser)*/
            DataResult.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deleteComment( /*TODO*/
        postId: String,
        commentId: String
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val commentRef =
                postsCollection.document(postId).collection("comments").document(commentId)

            val result = firestore.runTransaction {
                commentRef.delete()
                postsCollection.document(postId)
                    .update(mapOf("commentsCount" to FieldValue.increment(-1)))
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun likeCommentOrReply( /*TODO*/
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val userId =
                preferences.getUserId() ?: return DataResult.Error(DataError.Remote.UNKNOWN)
            val timestamp = Clock.System.now().toEpochMilliseconds()

            firestore.runTransaction {
                val baseRef = postsCollection
                    .document(postId)
                    .collection("comments")
                    .document(commentId)

                val targetRef = replyId?.let {
                    baseRef.collection("replies").document(it)
                } ?: baseRef

                val likesRef = targetRef.collection("likes").document(userId)
                val userLikesRef = firestore.collection("users")
                    .document(userId)
                    .collection(if (replyId != null) "likedReplies" else "likedComments")
                    .document(replyId ?: commentId)

                // 1. Guardar like en el comentario o reply
                likesRef.set(mapOf("timestamp" to timestamp))

                // 2. Incrementar contador
                targetRef.update("likesCount" to FieldValue.increment(1))

                // 3. Guardar referencia en el usuario
                userLikesRef.set(mapOf("timestamp" to timestamp, "postId" to postId))
            }

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun removeLikeCommentOrReply( /*TODO*/
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val userId =
                preferences.getUserId() ?: return DataResult.Error(DataError.Remote.UNKNOWN)

            firestore.runTransaction {
                val baseRef = postsCollection
                    .document(postId)
                    .collection("comments")
                    .document(commentId)

                val targetRef = replyId?.let {
                    baseRef.collection("replies").document(it)
                } ?: baseRef

                val likesRef = targetRef.collection("likes").document(userId)
                val userLikesRef = firestore.collection("users")
                    .document(userId)
                    .collection(if (replyId != null) "likedReplies" else "likedComments")
                    .document(replyId ?: commentId)

                // 1. Quitar like en el comentario o reply
                likesRef.delete()

                // 2. Decrementar contador
                targetRef.update("likesCount" to FieldValue.increment(-1))

                // 3. Quitar referencia del usuario
                userLikesRef.delete()
            }

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getLikedItemsForCurrentUser(postId: String): LikedItems {
        val userId = preferences.getUserId() ?: return LikedItems(emptySet(), emptySet())

        return try {
            val likedComments = firestore
                .collection("users")
                .document(userId)
                .collection("likedComments")
                .get()
                .documents
                .filter { (it.get("postId") as? String) == postId }
                .map { it.id }
                .toSet()

            val likedReplies = firestore
                .collection("users")
                .document(userId)
                .collection("likedReplies")
                .get()
                .documents
                .filter { (it.get("postId") as? String) == postId }
                .map { it.id }
                .toSet()

            LikedItems(likedComments, likedReplies)
        } catch (e: Exception) {
            LikedItems(emptySet(), emptySet())
        }
    }

    override suspend fun getComments(
        postId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<CommentWithExtras>, DataError.Remote> = safeCall {
        httpClient.get("${baseUrl()}/comments/post/$postId") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            parameter("limit", limit)
            cursorCreatedAt?.let { parameter("cursorCreatedAt", it) }
        }
    }

    /*override suspend fun getComments(
        postId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return try {
            val likedItems = getLikedItemsForCurrentUser(postId = postId)

            var query = postsCollection.document(postId)
                .collection("comments")
                .orderBy("timestamp", Direction.DESCENDING)
                .limit(limit.toLong())

            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val comments = query.get().documents.map { doc ->
                val timestamp = doc.get("timestamp") as? Timestamp
                val timestampMillis = timestamp?.toMilliseconds()?.toLong()
                val commentId = doc.get("id") as? String ?: doc.id

                Comment(
                    id = commentId,
                    userId = doc.get("userId") as? String ?: "",
                    text = doc.get("text") as? String ?: "",
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = doc.get("likesCount") ?: 0,
                    likedByUser = likedItems.likedCommentIds.contains(commentId),
                    replies = emptyList(),
                    repliesCount = doc.get("repliesCount") ?: 0,
                )
            }

            val uniqueUserIds = comments.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val commentsWithUser =
                comments.map { comment -> CommentWithUser(comment, userMap[comment.userId]) }

            DataResult.Success(commentsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }*/

    override suspend fun getReplies(
        commentId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<CommentWithExtras>, DataError.Remote> = safeCall {
        httpClient.get("${baseUrl()}/comments/replies/$commentId") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
            parameter("limit", limit)
            cursorCreatedAt?.let { parameter("cursorCreatedAt", it) }
        }
    }

    /*override suspend fun getReplies(
        postId: String,
        commentId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<CommentWithExtras>, DataError.Remote> {
        return try {
            /*val likedItems = getLikedItemsForCurrentUser(postId)

            var query = postsCollection.document(postId)
                .collection("comments").document(commentId)
                .collection("replies")
                .orderBy("timestamp", Direction.ASCENDING)
                .limit(limit.toLong())

            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val replies = query.get().documents.map { doc ->
                val timestamp = doc.get("timestamp") as? Timestamp
                val timestampMillis = timestamp?.toMilliseconds()?.toLong()
                val replyId = doc.get("id") as? String ?: doc.id

                Comment(
                    id = replyId,
                    userId = doc.get("userId") as? String ?: "",
                    text = doc.get("text") as? String ?: "",
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = doc.get("likesCount") ?: 0,
                    likedByUser = likedItems.likedReplyIds.contains(replyId),
                    replies = emptyList(),
                    repliesCount = 0,
                )
            }

            val uniqueUserIds = replies.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val repliesWithUser =
                replies.map { reply -> CommentWithUser(reply, userMap[reply.userId]) }

            DataResult.Success(repliesWithUser)*/
            DataResult.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }*/

    /*override suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            /*val userId = preferences.getUserId() ?: ""

            usersCollection.document(userId)
                .update("postsCount" to FieldValue.increment(-1))

            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get()
            if (postSnapshot.exists) {
                val post = postSnapshot.data<Post>()

                post..forEach { url ->
                    val fileName = url.substringAfterLast("/")
                    storageRef.child(fileName).delete()
                }

                val result = postRef.delete()
                DataResult.Success(result)
            }
            DataResult.Success(Unit)*/
            DataResult.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }*/

    override suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote> = safeCall {
        httpClient.delete("${baseUrl()}/posts/$postId") {
            header(HttpHeaders.Authorization, "Bearer ${authToken()}")
        }
    }

    override suspend fun reportPost(
        postId: String,
        reason: String
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val reportRef = postsCollection.document(postId)
                .collection("reports").document(userId)
            val result = reportRef.set(
                mapOf(
                    "reason" to reason,
                    "timestamp" to Clock.System.now().toEpochMilliseconds()
                )
            )
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getAllTags(): DataResult<List<Tag>, DataError.Remote> {
        return try {
            val result = tagsCollection
                .orderBy("usageCount", Direction.DESCENDING) // 🔽 los más usados primero
                .limit(50) // 🔹 ajustable según tu caso
                .get()
                .documents
                .map { it.data<Tag>() }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun createOrUpdateTags(tags: List<String>): DataResult<Unit, DataError.Remote> {
        return try {
            val result = tags.map { it.trim().lowercase() }
                .distinct()
                .forEach { createOrUpdateTag(it) }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    private suspend fun createOrUpdateTag(tag: String): DataResult<Unit, DataError.Remote> {
        return try {
            val normalizedName = tag.trim().lowercase()
            val docRef = tagsCollection.document(normalizedName)

            val snapshot = docRef.get()

            val result = if (snapshot.exists) {
                docRef.update("usageCount" to FieldValue.increment(1))
            } else {
                val newTag = Tag(
                    name = normalizedName,
                    usageCount = 1
                )
                docRef.set(newTag)
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    private suspend fun getUserById(userId: String): User? {
        val snapshot = usersCollection.document(userId).get()
        return if (snapshot.exists) snapshot.data<User>() else null
    }
}

data class LikedItems(
    val likedCommentIds: Set<String>,
    val likedReplyIds: Set<String>
)