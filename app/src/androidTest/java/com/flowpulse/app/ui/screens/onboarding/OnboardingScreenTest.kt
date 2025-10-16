package com.flowpulse.app.ui.screens.onboarding

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class OnboardingScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shows_onboarding_messages() {
        composeRule.setContent {
            OnboardingScreen(onContinue = {})
        }

        composeRule.onNodeWithText("Controla n8n desde el móvil").assertIsDisplayed()
        composeRule.onNodeWithText("Comenzar").assertIsDisplayed()
    }
}
