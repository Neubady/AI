package com.flowpulse.app.ui.viewmodel.model

sealed interface MainAction {
    data class UpdateDestination(val route: String) : MainAction
    data class RequestBiometricUnlock(val required: Boolean) : MainAction
}
