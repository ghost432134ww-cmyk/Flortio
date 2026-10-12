package com.hnfak.flortio

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hnfak.flortio.ui.navigation.FlortioRoot
import com.hnfak.flortio.ui.theme.FlortioTheme

/** Uygulamanın tek Activity'si; tüm ekranlar Compose Navigation ile yönetilir. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)

        val container = (application as FlortioApp).container
        setContent {
            FlortioTheme {
                FlortioRoot(container = container)
            }
        }
    }
}
