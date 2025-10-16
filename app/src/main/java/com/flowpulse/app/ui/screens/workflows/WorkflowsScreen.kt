package com.flowpulse.app.ui.screens.workflows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R
import com.flowpulse.app.domain.model.Workflow
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun WorkflowsScreen(
    instanceId: Long,
    onNavigateToWorkflow: (Long) -> Unit,
    viewModel: WorkflowsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(instanceId) { viewModel.observe(instanceId) }
    LaunchedEffect(state.error) {
        state.error?.let { message -> scope.launch { snackbarHostState.showSnackbar(message) } }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.workflows_title)) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(padding).padding(16.dp))
        }
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.workflows) { workflow ->
                WorkflowRow(
                    workflow = workflow,
                    onToggle = { activate -> viewModel.toggle(instanceId, workflow.id, activate) },
                    onClick = { onNavigateToWorkflow(workflow.id) }
                )
            }
        }
    }
}

@Composable
private fun WorkflowRow(
    workflow: Workflow,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
    ) {
        Text(text = workflow.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = if (workflow.active) stringResource(id = R.string.enable) else stringResource(id = R.string.disable))
            Switch(checked = workflow.active, onCheckedChange = onToggle)
        }
    }
}
