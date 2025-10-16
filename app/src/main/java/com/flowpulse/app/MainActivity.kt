package com.flowpulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import com.flowpulse.app.ui.navigation.FlowPulseNavHost
import com.flowpulse.app.ui.theme.FlowPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FlowPulseTheme {
                Surface {
                    FlowPulseNavHost()
                }
            }
        }
    }
}
