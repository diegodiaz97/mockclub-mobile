package com.diego.futty.home.postCreation.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.firebase.ImageUploader
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.Comment
import com.diego.futty.home.postCreation.domain.model.CommentWithUser
import com.diego.futty.home.postCreation.domain.model.Post
import com.diego.futty.home.postCreation.domain.model.PostWithUser
import com.diego.futty.home.postCreation.domain.model.Tag
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.datetime.Clock

class KtorRemotePostDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemotePostDataSource {

    private val firestore = firebaseManager.firestore
    private val usersCollection = firestore.collection("users")
    private val postsCollection = firestore.collection("posts")
    private val tagsCollection = firestore.collection("tags")
    private val storage = firebaseManager.storage
    private val storageRef = storage.reference("images/posts")

    override suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        ratio: Float,
        team: String,
        brand: String,
        tags: List<String>,
    ): DataResult<Unit, DataError.Remote> {
        return try {
            require(images.size <= 10) { "Máximo 10 imágenes por post" }
            val userId = preferences.getUserId() ?: ""

            usersCollection.document(userId)
                .update("postsCount" to FieldValue.increment(1))

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

            val docRef = postsCollection.document
            val result = docRef.set(
                mapOf(
                    "id" to docRef.id,
                    "userId" to userId,
                    "text" to text,
                    "imageUrls" to imageUrls,
                    "ratio" to ratio,
                    "timestamp" to FieldValue.serverTimestamp,
                    "likesCount" to 0,
                    "team" to team,
                    "brand" to brand,
                    "tags" to tags
                )
            )

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun countPosts(userId: String): DataResult<Int, DataError.Remote> {
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

    override suspend fun getLikedPostIdsForUser(userId: String): Set<String> {
        return try {
            val likedDocs = firestore.collection("users")
                .document(userId)
                .collection("likedPosts")
                .get()
                .documents

            likedDocs.map { it.id }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    override suspend fun getFeed(
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            var query =
                postsCollection.orderBy("timestamp", Direction.DESCENDING).limit(limit.toLong())
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
                    imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList(),
                    ratio = doc.get("ratio") as? Float ?: 1f,
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = (doc.get("likesCount") as? Int) ?: 0,
                    likedByUser = likedByUser,
                    team = doc.get("team") as? String ?: "",
                    brand = doc.get("brand") as? String ?: ""
                )
            }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
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
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            var query =
                postsCollection.where { "userId" equalTo userId }
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
                    imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList(),
                    ratio = doc.get("ratio") as? Float ?: 1f,
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = (doc.get("likesCount") as? Int) ?: 0,
                    likedByUser = likedByUser,
                    team = doc.get("team") as? String ?: "",
                    brand = doc.get("brand") as? String ?: ""
                )
            }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getPostsByTeam(
        team: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
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
                    imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList(),
                    ratio = doc.get("ratio") as? Float ?: 1f,
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = (doc.get("likesCount") as? Int) ?: 0,
                    likedByUser = likedByUser,
                    team = doc.get("team") as? String ?: "",
                    brand = doc.get("brand") as? String ?: ""
                )
            }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getPostsByBrand(
        brand: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
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
                    imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList(),
                    ratio = doc.get("ratio") as? Float ?: 1f,
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
                    likesCount = (doc.get("likesCount") as? Int) ?: 0,
                    likedByUser = likedByUser,
                    team = doc.get("team") as? String ?: "",
                    brand = doc.get("brand") as? String ?: ""
                )
            }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId =
                preferences.getUserId() ?: return DataResult.Error(DataError.Remote.UNKNOWN)
            val timestamp = Clock.System.now().toEpochMilliseconds()

            firestore.runTransaction {
                // 1. Agregar like al post
                postsCollection
                    .document(postId)
                    .collection("likes")
                    .document(userId)
                    .set(mapOf("timestamp" to timestamp))

                // 2. Incrementar contador
                postsCollection
                    .document(postId)
                    .update(mapOf("likesCount" to FieldValue.increment(1)))

                // 3. Guardar like en el usuario
                firestore.collection("users")
                    .document(userId)
                    .collection("likedPosts")
                    .document(postId)
                    .set(mapOf("timestamp" to timestamp))
            }

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }


    override suspend fun removeLike(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId =
                preferences.getUserId() ?: return DataResult.Error(DataError.Remote.UNKNOWN)
            firestore.runTransaction {
                // 1. Quitar el like del post
                postsCollection
                    .document(postId)
                    .collection("likes")
                    .document(userId)
                    .delete()

                // 2. Decrementar contador
                postsCollection
                    .document(postId)
                    .update(mapOf("likesCount" to FieldValue.increment(-1)))

                // 3. Eliminar el like del usuario
                firestore.collection("users")
                    .document(userId)
                    .collection("likedPosts")
                    .document(postId)
                    .delete()
            }
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun isPostLikedByUser(postId: String): DataResult<Boolean, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val result = postsCollection.document(postId)
                .collection("likes").document(userId).get().exists
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun addComment(
        postId: String,
        text: String
    ): DataResult<CommentWithUser, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val commentRef = postsCollection.document(postId).collection("comments").document

            firestore.runTransaction {
                commentRef.set(
                    mapOf(
                        "id" to commentRef.id,
                        "userId" to userId,
                        "text" to text,
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
            )

            val user = getUserById(userId)
            val commentsWithUser = CommentWithUser(comment, user)

            DataResult.Success(commentsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<Comment, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val replyRef = postsCollection.document(postId)
                .collection("comments").document(commentId)
                .collection("replies").document

            val reply = Comment(
                id = replyRef.id,
                userId = userId,
                text = text,
                timestamp = Clock.System.now()
                    .toEpochMilliseconds(), // no problem, it's only to show the first time
            )

            replyRef.set(
                mapOf(
                    "id" to replyRef.id,
                    "userId" to userId,
                    "text" to text,
                    "timestamp" to FieldValue.serverTimestamp, //save firebase timestamp
                )
            )
            DataResult.Success(reply)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deleteComment(
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

    override suspend fun likeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val target = if (replyId == null) {
                postsCollection.document(postId)
                    .collection("comments").document(commentId)
                    .collection("likes").document(userId)
            } else {
                postsCollection.document(postId)
                    .collection("comments").document(commentId)
                    .collection("replies").document(replyId)
                    .collection("likes").document(userId)
            }

            val result = target.set(mapOf("timestamp" to Clock.System.now().toEpochMilliseconds()))
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getComments(
        postId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return try {
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

                Comment(
                    id = doc.get("id") as? String ?: doc.id,
                    userId = doc.get("userId") as? String ?: "",
                    text = doc.get("text") as? String ?: "",
                    timestamp = timestampMillis ?: 0L,
                    serverTimestamp = doc.get("timestamp"),
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
    }

    override suspend fun getReplies(
        postId: String,
        commentId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return try {
            var query = postsCollection.document(postId)
                .collection("comments").document(commentId)
                .collection("replies")
                .orderBy("timestamp", Direction.DESCENDING)
                .limit(limit.toLong())

            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val replies = query.get().documents.map { it.data<Comment>() }

            val uniqueUserIds = replies.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val repliesWithUser =
                replies.map { reply -> CommentWithUser(reply, userMap[reply.userId]) }

            DataResult.Success(repliesWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""

            usersCollection.document(userId)
                .update("postsCount" to FieldValue.increment(-1))

            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get()
            if (postSnapshot.exists) {
                val post = postSnapshot.data<Post>()

                post.imageUrls.forEach { url ->
                    val fileName = url.substringAfterLast("/")
                    storageRef.child(fileName).delete()
                }

                val result = postRef.delete()
                DataResult.Success(result)
            }
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
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
