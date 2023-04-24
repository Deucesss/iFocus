package com.rencaihu.timer.ui.ongoingtimer

interface TimerListener {

    fun timerAdded(timer: Timer)

    fun timerUpdated(timer: Timer)

    fun timerRemoved(timer: Timer)

}