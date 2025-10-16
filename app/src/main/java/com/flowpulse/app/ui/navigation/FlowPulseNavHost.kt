package com.flowpulse.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flowpulse.app.ui.screens.actions.ActionsScreen
import com.flowpulse.app.ui.screens.alerts.AlertsScreen
import com.flowpulse.app.ui.screens.dashboard.DashboardScreen
import com.flowpulse.app.ui.screens.executions.ExecutionDetailScreen
import com.flowpulse.app.ui.screens.executions.ExecutionsScreen
import com.flowpulse.app.ui.screens.instances.InstancesScreen
import com.flowpulse.app.ui.screens.onboarding.OnboardingScreen
import com.flowpulse.app.ui.screens.settings.SettingsScreen
import com.flowpulse.app.ui.screens.workflows.WorkflowDetailScreen
import com.flowpulse.app.ui.screens.workflows.WorkflowsScreen

sealed class FlowPulseDestination(val route: String) {
    data object Onboarding : FlowPulseDestination("onboarding")
    data object Instances : FlowPulseDestination("instances")
    data class Dashboard(val instanceId: Long) : FlowPulseDestination("dashboard/{instanceId}") {
        companion object {
            const val ROUTE = "dashboard/{instanceId}"
            fun create(instanceId: Long) = "dashboard/$instanceId"
        }
    }
    data class WorkflowDetail(val instanceId: Long, val workflowId: Long) : FlowPulseDestination("workflow/{instanceId}/{workflowId}") {
        companion object {
            const val ROUTE = "workflow/{instanceId}/{workflowId}"
            fun create(instanceId: Long, workflowId: Long) = "workflow/$instanceId/$workflowId"
        }
    }
    data class ExecutionDetail(val instanceId: Long, val executionId: Long) : FlowPulseDestination("execution/{instanceId}/{executionId}") {
        companion object {
            const val ROUTE = "execution/{instanceId}/{executionId}"
            fun create(instanceId: Long, executionId: Long) = "execution/$instanceId/$executionId"
        }
    }
    data object Workflows : FlowPulseDestination("workflows/{instanceId}")
    data object Executions : FlowPulseDestination("executions/{instanceId}")
    data object Alerts : FlowPulseDestination("alerts/{instanceId}")
    data object Actions : FlowPulseDestination("actions/{instanceId}")
    data object Settings : FlowPulseDestination("settings")
}

@Composable
fun FlowPulseNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = FlowPulseDestination.Onboarding.route
    ) {
        composable(FlowPulseDestination.Onboarding.route) {
            OnboardingScreen(onFinished = { navController.navigate(FlowPulseDestination.Instances.route) })
        }
        composable(FlowPulseDestination.Instances.route) {
            InstancesScreen(
                onInstanceSelected = { navController.navigate(FlowPulseDestination.Dashboard.create(it)) },
                onOpenSettings = { navController.navigate(FlowPulseDestination.Settings.route) }
            )
        }
        composable(FlowPulseDestination.Dashboard.ROUTE) {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            DashboardScreen(
                instanceId = instanceId,
                onViewWorkflows = { navController.navigate("workflows/$instanceId") },
                onViewExecutions = { navController.navigate("executions/$instanceId") },
                onViewAlerts = { navController.navigate("alerts/$instanceId") },
                onViewActions = { navController.navigate("actions/$instanceId") }
            )
        }
        composable("workflows/{instanceId}") {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            WorkflowsScreen(
                instanceId = instanceId,
                onNavigateToWorkflow = { workflowId ->
                    navController.navigate(FlowPulseDestination.WorkflowDetail.create(instanceId, workflowId))
                }
            )
        }
        composable(FlowPulseDestination.WorkflowDetail.ROUTE) {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            val workflowId = it.arguments?.getString("workflowId")?.toLongOrNull() ?: return@composable
            WorkflowDetailScreen(instanceId = instanceId, workflowId = workflowId)
        }
        composable("executions/{instanceId}") {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            ExecutionsScreen(
                instanceId = instanceId,
                onExecutionSelected = { executionId ->
                    navController.navigate(FlowPulseDestination.ExecutionDetail.create(instanceId, executionId))
                }
            )
        }
        composable(FlowPulseDestination.ExecutionDetail.ROUTE) {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            val executionId = it.arguments?.getString("executionId")?.toLongOrNull() ?: return@composable
            ExecutionDetailScreen(instanceId = instanceId, executionId = executionId)
        }
        composable("alerts/{instanceId}") {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            AlertsScreen(instanceId = instanceId)
        }
        composable("actions/{instanceId}") {
            val instanceId = it.arguments?.getString("instanceId")?.toLongOrNull() ?: return@composable
            ActionsScreen(instanceId = instanceId)
        }
        composable(FlowPulseDestination.Settings.route) {
            SettingsScreen()
        }
    }
}
