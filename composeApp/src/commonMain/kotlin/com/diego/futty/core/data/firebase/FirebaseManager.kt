package com.diego.futty.core.data.firebase

import com.tweener.passage.Passage
import com.tweener.passage.model.AppleGatekeeperConfiguration
import com.tweener.passage.model.EmailPasswordGatekeeperConfiguration
import com.tweener.passage.model.GoogleGatekeeperAndroidConfiguration
import com.tweener.passage.model.GoogleGatekeeperConfiguration
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.messaging.messaging
import dev.gitlive.firebase.storage.storage

object FirebaseManager {
    val auth = Firebase.auth
    val firestore = Firebase.firestore
    val storage = Firebase.storage
    val messaging = Firebase.messaging
    val passage = providePassage()
}

expect fun providePassage(): Passage
