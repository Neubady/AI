package com.flowpulse.app.ui.screens.executions

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun ExecutionDetailScreen(
    instanceId: Long,
    executionId: Long,
    viewModel: ExecutionDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(executionId) { viewModel.load(instanceId, executionId) }

    Scaffold(topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.executions_title)) }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> Text(text = stringResource(id = R.string.refresh))
                state.error != null -> Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error)
                state.execution != null -> {
                    val execution = state.execution
                    Text(text = "#${execution.id}", style = MaterialTheme.typography.titleLarge)
                    execution.startedAt?.let {
                        val local = it.toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(text = "${local.date} ${local.time}")
                    }
                    execution.error?.message?.let { error ->
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = {
                        val payload = Json.encodeToString(execution)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_TEXT, payload)
                        }
                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.execution_detail_share)))
                    }) {
                        Text(text = stringResource(id = R.string.execution_detail_share))
                    }
                }
            }
        }
    }
}
