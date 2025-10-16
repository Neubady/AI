package com.flowpulse.app.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "KPIs principales", style = MaterialTheme.typography.headlineSmall)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Workflows activos: 0", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Ejecuciones hoy: 0", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Errores hoy: 0", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Latencia promedio: -- ms", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
