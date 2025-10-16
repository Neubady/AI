package com.flowpulse.app.ui.screens.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R
import com.flowpulse.app.domain.model.Alert
import com.flowpulse.app.domain.model.AlertSeverity
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AlertsScreen(
    instanceId: Long,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(instanceId) { viewModel.observe(instanceId) }

    Scaffold(topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.alerts_title)) }) }) { padding ->
        if (state.alerts.isEmpty()) {
            Text(
                text = stringResource(id = R.string.alert_no_results),
                modifier = Modifier.padding(padding).padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(state.alerts) { alert -> AlertRow(alert = alert) }
            }
        }
    }
}

@Composable
private fun AlertRow(alert: Alert) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = alert.title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = alert.message, style = MaterialTheme.typography.bodyMedium)
        val date = alert.createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
        Text(text = "${date.date} ${date.time}", style = MaterialTheme.typography.labelMedium)
        val color = when (alert.severity) {
            AlertSeverity.ERROR -> MaterialTheme.colorScheme.error
            AlertSeverity.WARNING -> MaterialTheme.colorScheme.tertiary
            AlertSeverity.INFO -> MaterialTheme.colorScheme.primary
        }
        Text(text = alert.severity.name, color = color)
    }
}
