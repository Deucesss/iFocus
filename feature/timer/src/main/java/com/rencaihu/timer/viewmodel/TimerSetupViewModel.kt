package com.rencaihu.timer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerSetupViewModel: ViewModel() {

    enum class DisplayMode(val viewPosition: Int) {
        CLOCK(0), PICKER(1)
    }

    private var _displayMode: MutableStateFlow<DisplayMode> = MutableStateFlow(DisplayMode.CLOCK)
    val displayMode: StateFlow<DisplayMode>
        get() = _displayMode

    fun switchDisplayMode() {
        _displayMode.value = if (_displayMode.value == DisplayMode.CLOCK) {
            DisplayMode.PICKER
        } else {
            DisplayMode.CLOCK
        }
    }

}