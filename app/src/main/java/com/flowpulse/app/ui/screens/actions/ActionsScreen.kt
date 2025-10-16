package com.flowpulse.app.ui.screens.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R
import com.flowpulse.app.domain.model.QuickAction
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ActionsScreen(
    instanceId: Long,
    viewModel: ActionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(instanceId) { viewModel.observe(instanceId) }

    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.actions_title)) }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.actions_title)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text(stringResource(id = R.string.actions_webhook_url)) },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                if (name.isNotBlank() && url.isNotBlank()) {
                    viewModel.save(QuickAction(id = 0, instanceId = instanceId, name = name, method = "POST", url = url))
                    name = ""
                    url = ""
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.actions_add))
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.actions) { action ->
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(text = action.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = action.url, style = MaterialTheme.typography.bodyMedium)
                        OutlinedButton(onClick = { viewModel.delete(action.id) }) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            }
        }
    }
}
