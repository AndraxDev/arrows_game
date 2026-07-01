package com.batodev.arrows

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InitializationManager {
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    fun markInitialized() {
        if (!_isInitialized.value) {
            _isInitialized.value = true
        }
    }

    fun reset() {
        _isInitialized.value = false
    }
}
