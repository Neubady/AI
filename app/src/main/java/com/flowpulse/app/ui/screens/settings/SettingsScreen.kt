package com.flowpulse.app.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.settings_title)) }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(text = stringResource(id = R.string.settings_theme), style = MaterialTheme.typography.titleLarge)
            Text(text = state.theme, style = MaterialTheme.typography.bodyMedium)
            OutlinedButton(onClick = { viewModel.setTheme("light") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.settings_theme_light))
            }
            OutlinedButton(onClick = { viewModel.setTheme("dark") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.settings_theme_dark))
            }
            OutlinedButton(onClick = { viewModel.setTheme("system") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.settings_theme_system))
            }
            Text(text = stringResource(id = R.string.settings_language), style = MaterialTheme.typography.titleLarge)
            Text(text = state.language.uppercase(), style = MaterialTheme.typography.bodyMedium)
            OutlinedButton(onClick = { viewModel.setLanguage("es") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.language_es))
            }
            OutlinedButton(onClick = { viewModel.setLanguage("en") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.language_en))
            }
            Text(text = stringResource(id = R.string.settings_billing), style = MaterialTheme.typography.titleLarge)
            Button(onClick = { /* Launch billing flow handled in activity */ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.paywall_title))
            }
        }
    }
}
