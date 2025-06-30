package com.diego.futty.core.data.firebase

import com.diego.futty.core.data.local.appContext
import com.tweener.passage.Passage
import com.tweener.passage.PassageAndroid

actual fun providePassage(): Passage = PassageAndroid(applicationContext = appContext)
