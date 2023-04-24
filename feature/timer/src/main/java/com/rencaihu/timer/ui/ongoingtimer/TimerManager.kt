package com.rencaihu.timer.ui.ongoingtimer

import android.content.Context
import androidx.annotation.Keep
import com.rencaihu.timer.datastore.asDomainModel
import com.rencaihu.timer.datastore.timerStore
import com.rencaihu.timer.ui.ongoingtimer.Timer.Companion.UNUSED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

class TimerManager {

    private var mTimer: Timer? = null
    private val mTimerListeners = mutableListOf<TimerListener>()

    private var mContext: Context? = null
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    fun init(context: Context) {
        if (mContext !== context) {
            mContext = context
            readTimerFromDatastore()
        }
    }

    private fun readTimerFromDatastore() {
        scope.launch {
            val timerProto = mContext!!.timerStore.data.firstOrNull()
            if (timerProto?.state != 0) {
                mTimer = timerProto?.asDomainModel()
            }
            Timber.d("Timer: $mTimer")
        }

    }

    private fun saveTimerToDatastore() {
        scope.launch {
            mTimer?.let { timer ->
                mContext!!.timerStore.updateData { preferences ->
                    preferences.toBuilder()
                        .setId(timer.id)
                        .setState(timer.state.value)
                        .setDurationPerLap(timer.durationPerLap)
                        .setLaps(timer.laps)
                        .setLastLap(timer.lastLap)
                        .setLastStartTime(timer.lastStartTime)
                        .setLastRemainingTime(timer.lastRemainingTime)
                        .build()
                }
            }
        }
    }

    private fun removeTimerFromDatastore() {
        scope.launch {
            mContext!!.timerStore.updateData { preferences ->
                preferences.toBuilder().clear().build()
            }
        }
    }

    fun addTimerListener(timerListener: TimerListener) {
        mTimerListeners.add(timerListener)
    }

    fun removeTimerListener(timerListener: TimerListener) {
        mTimerListeners.remove(timerListener)
    }

    fun newTimer(durationPerLap: Long, laps: Int): Timer {
        val timer = Timer(-1, Timer.State.READY, durationPerLap, laps, 1, UNUSED, durationPerLap)
        Timber.d("New Timer: $timer")
        mTimer = timer
        saveTimerToDatastore()
        return timer
    }

    fun getTimer(): Timer? {
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

        saveTimerToDatastore()

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