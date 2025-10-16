package com.flowpulse.app.ui.screens.instances

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.BuildConfig
import com.flowpulse.app.domain.model.AuthType
import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.ui.components.BannerAdView
import com.flowpulse.app.R
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstancesScreen(
    onInstanceSelected: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: InstancesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.value.error) {
        state.value.error?.let { message ->
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.instances_title)) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.resetForm()
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.value.instances.isEmpty()) {
                Text(text = stringResource(id = R.string.instances_empty))
            }
            LazyColumn(
                modifier = Modifier.weight(1f, fill = true),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.value.instances) { instance ->
                    InstanceCard(
                        instance = instance,
                        onClick = { onInstanceSelected(instance.id) },
                        onEdit = {
                            viewModel.editInstance(instance)
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteInstance(instance.id) }
                    )
                }
            }
            if (!state.value.isLimitReached) {
                val bannerId = if (BuildConfig.DEBUG || BuildConfig.ADMOB_BANNER_UNIT_ID.isBlank()) {
                    "ca-app-pub-3940256099942544/6300978111"
                } else BuildConfig.ADMOB_BANNER_UNIT_ID
                BannerAdView(
                    modifier = Modifier.fillMaxWidth(),
                    adUnitId = bannerId
                )
            }
        }
    }

    if (showDialog) {
        InstanceDialog(
            state = state.value.form,
            isSaving = state.value.isSaving,
            onDismiss = { showDialog = false },
            onSave = {
                viewModel.saveInstance()
                showDialog = false
            },
            onNameChange = viewModel::onNameChange,
            onUrlChange = viewModel::onUrlChange,
            onTokenChange = viewModel::onTokenChange,
            onAuthTypeSelected = viewModel::onAuthTypeSelected
        )
    }
}

@Composable
private fun InstanceCard(
    instance: N8nInstance,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = instance.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = instance.baseUrl, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onEdit) { Text(text = stringResource(id = R.string.edit)) }
                OutlinedButton(onClick = onDelete) { Text(text = stringResource(id = R.string.delete)) }
            }
        }
    }
}

@Composable
private fun InstanceDialog(
    state: InstanceFormState,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onTokenChange: (String) -> Unit,
    onAuthTypeSelected: (AuthType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onSave, enabled = !isSaving) { Text(text = stringResource(id = R.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(id = R.string.cancel)) }
        },
        title = { Text(text = if (state.id == 0L) stringResource(id = R.string.instances_add) else stringResource(id = R.string.instances_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(id = R.string.instances_title)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.baseUrl,
                    onValueChange = onUrlChange,
                    label = { Text("Base URL") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.token,
                    onValueChange = onTokenChange,
                    label = { Text("API Key / Token") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Basic Auth")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = state.authType == AuthType.BASIC,
                        onCheckedChange = { onAuthTypeSelected(if (it) AuthType.BASIC else AuthType.BEARER) }
                    )
                }
            }
        }
    )
}
