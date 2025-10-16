package com.flowpulse.app.ui.screens.instances

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flowpulse.app.domain.model.FlowPulseAppState
import com.flowpulse.app.ui.viewmodel.model.MainState

@Composable
fun InstancesScreen(state: MainState, onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        when (val appState = state.appState) {
            FlowPulseAppState.Loading -> Text(text = "Cargando...", style = MaterialTheme.typography.bodyMedium)
            is FlowPulseAppState.Locked -> Text(text = "Bloqueado por seguridad", style = MaterialTheme.typography.bodyMedium)
            is FlowPulseAppState.Ready -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(appState.instances) { index ->
                        Card(onClick = { onNavigate("dashboard/$index") }) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Instancia #${index + 1}", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Estado: OK", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onNavigate("actions/0") }) {
                    Text(text = "Ir a acciones rápidas")
                }
            }
        }
    }
}
