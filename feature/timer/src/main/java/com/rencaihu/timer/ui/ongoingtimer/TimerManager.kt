package com.rencaihu.timer.ui.ongoingtimer

import androidx.annotation.Keep
import com.rencaihu.timer.ui.ongoingtimer.Timer1.Companion.UNUSED

class TimerManager {

    private var mTimer: Timer1? = null
    private val mTimerListeners = mutableListOf<TimerListener>()

    fun addTimerListener(timerListener: TimerListener) {
        mTimerListeners.add(timerListener)
    }

    fun removeTimerListener(timerListener: TimerListener) {
        mTimerListeners.remove(timerListener)
    }

    fun newTimer(durationPerLap: Long, laps: Int): Timer1 {
        val timer = Timer1(-1, Timer1.State.READY, durationPerLap, laps, 1, UNUSED, durationPerLap)
        mTimer = timer
        return timer
    }

    fun getTimer(timerId: Int): Timer1? {
        return mTimer
    }

    fun startTimer(timer: Timer1) {
        updateTimer(timer.start())
    }

    fun pauseTimer(timer: Timer1) {
        updateTimer(timer.pause())
    }

    private fun updateTimer(timer: Timer1): Timer1 {
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