package com.rencaihu.timer.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.Keep
import com.rencaihu.timer.ui.ongoingtimer.Timer
import com.rencaihu.timer.ui.ongoingtimer.TimerListener
import com.rencaihu.timer.ui.ongoingtimer.TimerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimerManager {

    private var mTimer: Timer? = null
    private val mTimerListeners = mutableListOf<TimerListener>()

    private var mContext: Context? = null
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var mTimerDAO: TimerDAO? = null
    private lateinit var mAlarmManager: AlarmManager

    fun init(context: Context) {
        if (mContext !== context) {
            mContext = context
            mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mTimerDAO = TimerDAO(context)
            readTimerFromDatastore()
        }
    }

    private fun readTimerFromDatastore() {
        scope.launch {
            mTimer = mTimerDAO?.readTimer()
        }
    }

    private fun saveTimerToDatastore(timer: Timer) {
        scope.launch {
           mTimerDAO?.saveTimer(timer)
        }
    }

    private fun removeTimerFromDatastore() {
        scope.launch {
            mTimer?.let {
                mTimerDAO?.deleteTimer(it)
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
        val timer = Timer.newTimer(-1, durationPerLap, laps)
        mTimer = timer
        saveTimerToDatastore(timer)
        return timer
    }

    fun breakTimer(durationPerLap: Long): Timer {
        val timer = Timer.newBreakTimer(durationPerLap)
        saveTimerToDatastore(timer)
        readTimerFromDatastore()
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

    fun expireTimer(timer: Timer) {
        updateTimer(timer.expire())
    }

    private fun updateTimer(timer: Timer): Timer {
        val before = mTimer
        if (before === timer) {
            return timer
        }

        saveTimerToDatastore(timer)

        mTimer = timer

        for (timerListener in mTimerListeners) {
            timerListener.timerUpdated(mTimer!!)
        }
        updateAlarmManager()

        return mTimer!!
    }

    private fun updateAlarmManager() {
        var expiringTimer: Timer? = null
        if (mTimer != null && mTimer!!.isRunning) {
            expiringTimer = mTimer
        }

        if (expiringTimer != null) {
            val alarmIntent = TimerService.createExpireTimerIntent(mContext!!)
            val pendingIntent = PendingIntent.getService(
                mContext,
                0,
                alarmIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
            )
            mAlarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                expiringTimer.expirationTime,
                pendingIntent
            )
        } else {
            val alarmIntent = TimerService.createExpireTimerIntent(mContext!!)
            PendingIntent.getService(
                mContext,
                0,
                alarmIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_NO_CREATE
            )?.let {
                mAlarmManager.cancel(it)
                it.cancel()
            }
        }
    }

    companion object {
        private val sTimerManager: TimerManager = TimerManager()

        @get:JvmStatic
        @get:Keep
        val timerManager: TimerManager
            get() = sTimerManager
    }
}