package com.diego.futty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diego.futty.authentication.view.AuthenticationView
import com.diego.futty.core.data.local.appContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext

        setContent {
            AuthenticationView()
        }
    }
}
