package com.flowpulse.app.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.flowpulse.app.R

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = listOf(
        OnboardingPage(title = R.string.onboarding_title_1, body = R.string.onboarding_body_1),
        OnboardingPage(title = R.string.onboarding_title_2, body = R.string.onboarding_body_2),
        OnboardingPage(title = R.string.onboarding_title_3, body = R.string.onboarding_body_3)
    )
    val currentIndex = remember { mutableIntStateOf(0) }
    val isLast = currentIndex.intValue == pages.lastIndex

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = pages[currentIndex.intValue].title), style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = stringResource(id = pages[currentIndex.intValue].body), style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = {
                    if (isLast) onFinished() else currentIndex.intValue++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = if (isLast) R.string.onboarding_done else R.string.onboarding_next))
            }
        }
    }
}

data class OnboardingPage(val title: Int, val body: Int)
