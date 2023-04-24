package com.rencaihu.timer.ui.timersetup

import androidx.lifecycle.ViewModel
import com.rencaihu.timer.ui.ongoingtimer.Timer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerSetupViewModel: ViewModel() {

    enum class DisplayMode(val viewPosition: Int) {
        CLOCK(0), PICKER(1)
    }

    private var _uiState: MutableStateFlow<SetupUiState> = MutableStateFlow(SetupUiState(DisplayMode.CLOCK, Timer.newTimer(-1, 30 * 60 * 1000, 1)))
    val uiState: StateFlow<SetupUiState>
        get() = _uiState

    fun setDuration(duration: Int) {
        _uiState.value = _uiState.value.copy(timer = _uiState.value.timer.setDurationPerLap(duration * 60 * 1000L))
    }

    fun setLaps(laps: Int) {
        _uiState.value = _uiState.value.copy(timer = _uiState.value.timer.setLaps(laps))
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
            val timer: Timer
        )
    }
}