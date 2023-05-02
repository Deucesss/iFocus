package com.rencaihu.timer.ui.ongoingtimer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.rencaihu.timer.data.TimerManager
import timber.log.Timber

class TimerService : Service() {
    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_EXPIRE_TIMER -> {
                Timber.d("TimerService: expire timer")
                val timer = TimerManager.timerManager.getTimer()
                timer?.let {
                    TimerManager.timerManager.expireTimer(it)
                }
            }
        }


        return START_NOT_STICKY
    }

    companion object {
        const val ACTION_EXPIRE_TIMER = "com.rencaihu.timer.action.expire"

        fun createExpireTimerIntent(context: Context) =
            Intent(context, TimerService::class.java).apply {
                action = ACTION_EXPIRE_TIMER
            }
    }
}