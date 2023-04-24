package com.rencaihu.ifocus

import android.app.Application
import com.rencaihu.timer.ui.ongoingtimer.TimerManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        TimerManager.timerManager.init(this)
    }
}