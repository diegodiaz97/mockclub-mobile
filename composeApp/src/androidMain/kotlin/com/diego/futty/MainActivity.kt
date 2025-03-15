package com.diego.futty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diego.futty.app.App
import com.diego.futty.core.presentation.Grey0
import com.diego.futty.design.utils.SetSystemBarsColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SetSystemBarsColor(
                statusBarColor = Grey0,
                navigationBarColor = Grey0,
                isSystemDarkIcons = true
            )
            App()
        }
    }
}
