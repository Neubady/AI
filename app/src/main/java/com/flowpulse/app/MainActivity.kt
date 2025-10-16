package com.flowpulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.flowpulse.app.ui.navigation.FlowPulseNavHost
import com.flowpulse.app.ui.theme.FlowPulseTheme
import com.flowpulse.app.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowPulseTheme {
                Surface {
                    val state by viewModel.state.collectAsState()
                    LaunchedEffect(Unit) {
                        viewModel.onStart(intent?.data)
                    }
                    FlowPulseNavHost(state = state, onAction = viewModel::onAction)
                }
            }
        }
    }
}
