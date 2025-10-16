package com.flowpulse.app.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.flowpulse.app.MainActivity
import org.junit.Rule
import org.junit.Test

class MainActivityLaunchTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunches() {
        composeTestRule.onRoot().assertExists()
    }
}
