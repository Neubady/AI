package com.flowpulse.app.domain.model

sealed interface FlowPulseAppState {
    data object Loading : FlowPulseAppState
    data class Locked(val reason: String = "secure_lock") : FlowPulseAppState
    data class Ready(val hasPro: Boolean, val instances: Int) : FlowPulseAppState
}
