package com.rencaihu.timer.ui.ongoingtimer

interface TimerListener {

    fun timerAdded(timer: Timer1)

    fun timerUpdated(timer: Timer1)

    fun timerRemoved(timer: Timer1)

}