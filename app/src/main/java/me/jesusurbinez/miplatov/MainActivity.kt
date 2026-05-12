package me.jesusurbinez.miplatov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import me.jesusurbinez.miplatov.ui.navigation.MiPlatoApp
import me.jesusurbinez.miplatov.ui.theme.MiPlatoVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiPlatoVTheme {
                MiPlatoApp()
            }
        }
    }
}
