package com.rencaihu.timer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerSetupViewModel: ViewModel() {

    enum class DisplayMode(val viewPosition: Int) {
        CLOCK(0), PICKER(1)
    }

    private var _uiState: MutableStateFlow<SetupUiState> = MutableStateFlow(SetupUiState(DisplayMode.CLOCK, 30, 1))
    val uiState: StateFlow<SetupUiState>
        get() = _uiState

    fun setDuration(duration: Int) {
        _uiState.value = _uiState.value.copy(duration = duration)
    }

    fun setLaps(laps: Int) {
        _uiState.value = _uiState.value.copy(laps = laps)
    }

    fun switchDisplayMode() {
        val displayMode = if (_uiState.value.displayMode == DisplayMode.CLOCK) {
            DisplayMode.PICKER
        } else {
            DisplayMode.CLOCK
        }
        _uiState.value = _uiState.value.copy(displayMode = displayMode)
    }

    companion object {
        data class SetupUiState(
            val displayMode: DisplayMode,
            val duration: Int,
            val laps: Int
        )
    }
}