package com.flowpulse.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flowpulse.app.ui.screens.dashboard.DashboardScreen
import com.flowpulse.app.ui.screens.instances.InstancesScreen
import com.flowpulse.app.ui.screens.onboarding.OnboardingScreen
import com.flowpulse.app.ui.viewmodel.model.MainAction
import com.flowpulse.app.ui.viewmodel.model.MainState
import com.flowpulse.app.ui.viewmodel.model.FlowPulseDestinations

@Composable
fun FlowPulseNavHost(
    state: MainState,
    onAction: (MainAction) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    LaunchedEffect(state.currentDestination) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        val normalizedCurrent = currentRoute?.substringBefore("/")
        val normalizedTarget = state.currentDestination.substringBefore("/")
        if (normalizedCurrent != normalizedTarget || currentRoute != state.currentDestination) {
            navController.navigate(state.currentDestination) {
                launchSingleTop = true
                popUpTo(navController.graph.startDestinationId) { saveState = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = FlowPulseDestinations.Onboarding.route
    ) {
        composable("onboarding") {
            OnboardingScreen(onContinue = {
                onAction(MainAction.UpdateDestination("instances"))
            })
        }
        composable("instances") {
            InstancesScreen(state = state, onNavigate = { destination ->
                onAction(MainAction.UpdateDestination(destination))
            })
        }
        composable("dashboard/{instanceId}") {
            DashboardScreen()
        }
    }
}
