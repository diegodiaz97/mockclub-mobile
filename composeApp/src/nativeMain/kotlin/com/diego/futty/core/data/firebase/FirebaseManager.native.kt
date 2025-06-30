package com.diego.futty.core.data.firebase

import com.tweener.passage.Passage
import com.tweener.passage.PassageIos

actual fun providePassage(): Passage = PassageIos()
