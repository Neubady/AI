package com.flowpulse.app.ui.viewmodel.model

import com.flowpulse.app.domain.model.DeepLinkResult
import com.flowpulse.app.domain.model.FlowPulseAppState

data class MainState(
    val appState: FlowPulseAppState = FlowPulseAppState.Loading,
    val requireAuthentication: Boolean = false,
    val currentDestination: String = FlowPulseDestinations.Onboarding.route,
    val pendingDeepLink: DeepLinkResult? = null
)

sealed class FlowPulseDestinations(val route: String) {
    object Onboarding : FlowPulseDestinations("onboarding")
    object Instances : FlowPulseDestinations("instances")
    object Dashboard : FlowPulseDestinations("dashboard/{instanceId}")
    object Workflows : FlowPulseDestinations("workflows/{instanceId}")
    object Executions : FlowPulseDestinations("executions/{instanceId}")
    object Alerts : FlowPulseDestinations("alerts/{instanceId}")
    object Actions : FlowPulseDestinations("actions/{instanceId}")
    object Settings : FlowPulseDestinations("settings")
}
