package com.farmlife.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.farmlife.app.ui.screens.MainAppScreen
import com.farmlife.app.ui.theme.FarmLifeTheme

/**
 * 主Activity - Compose 入口
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FarmLifeTheme {
                val app = FarmLifeApplication.getInstance()
                MainAppScreen(engine = app.engine)
            }
        }
    }
}
