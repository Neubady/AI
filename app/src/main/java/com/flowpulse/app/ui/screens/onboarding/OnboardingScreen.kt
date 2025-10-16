package com.flowpulse.app.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.flowpulse.app.R

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = stringResource(id = R.string.onboarding_title_1), style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(id = R.string.onboarding_body_1), style = MaterialTheme.typography.bodyLarge)
            Text(text = stringResource(id = R.string.onboarding_title_2), style = MaterialTheme.typography.headlineSmall)
            Text(text = stringResource(id = R.string.onboarding_body_2), style = MaterialTheme.typography.bodyLarge)
            Text(text = stringResource(id = R.string.onboarding_title_3), style = MaterialTheme.typography.headlineSmall)
            Text(text = stringResource(id = R.string.onboarding_body_3), style = MaterialTheme.typography.bodyLarge)
        }
        Button(onClick = onContinue) {
            Text(text = "Comenzar")
        }
    }
}
