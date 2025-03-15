package com.diego.futty

import android.app.Application
import com.diego.futty.di.initKoin
import org.koin.android.ext.koin.androidContext

class FuttyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FuttyApplication)
        }
    }
}