package com.rencaihu.timer.ui.ongoingtimer

import androidx.annotation.Keep
import com.rencaihu.timer.ui.ongoingtimer.Timer.Companion.UNUSED

class TimerManager {

    private var mTimer: Timer? = null
    private val mTimerListeners = mutableListOf<TimerListener>()

    fun addTimerListener(timerListener: TimerListener) {
        mTimerListeners.add(timerListener)
    }

    fun removeTimerListener(timerListener: TimerListener) {
        mTimerListeners.remove(timerListener)
    }

    fun newTimer(durationPerLap: Long, laps: Int): Timer {
        val timer = Timer(-1, Timer.State.READY, durationPerLap, laps, 1, UNUSED, durationPerLap)
        mTimer = timer
        return timer
    }

    fun getTimer(timerId: Int): Timer? {
        return mTimer
    }

    fun startTimer(timer: Timer) {
        updateTimer(timer.start())
    }

    fun pauseTimer(timer: Timer) {
        updateTimer(timer.pause())
    }

    private fun updateTimer(timer: Timer): Timer {
        val before = mTimer
        if (before === timer) {
            return timer
        }

        // TODO: persist new timer

        mTimer = timer

        for (timerListener in mTimerListeners) {
            timerListener.timerUpdated(mTimer!!)
        }

        return mTimer!!
    }

    companion object {
        private val sTimerManager: TimerManager = TimerManager()

        @get:JvmStatic
        @get:Keep
        val timerManager: TimerManager
            get() = sTimerManager
    }
}