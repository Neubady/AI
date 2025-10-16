package com.flowpulse.app.ui.screens.executions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R
import com.flowpulse.app.domain.model.Execution
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone

@Composable
fun ExecutionsScreen(
    instanceId: Long,
    onExecutionSelected: (Long) -> Unit,
    viewModel: ExecutionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(instanceId) { viewModel.observe(instanceId) }
    LaunchedEffect(state.error) {
        state.error?.let { message -> scope.launch { snackbarHostState.showSnackbar(message) } }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.executions_title)) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.executions) { execution ->
                ExecutionRow(execution = execution, onClick = { onExecutionSelected(execution.id) })
            }
        }
    }
}

@Composable
private fun ExecutionRow(execution: Execution, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "#${execution.id}", style = MaterialTheme.typography.titleLarge)
            Text(text = execution.status, color = if (execution.status.equals("error", true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        }
        execution.startedAt?.let {
            val local = it.toLocalDateTime(TimeZone.currentSystemDefault())
            Text(text = "${local.date} ${local.time}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
