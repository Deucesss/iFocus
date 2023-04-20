package com.rencaihu.timer.ui.ongoingtimer

import android.os.CountDownTimer

class Timer(
    private var duration: Long,
    private val onActionTick: (millisUntilFinished: Long) -> Unit,
    private val onActionFinish: () -> Unit
): CountDownTimer(duration, 1000) {

    override fun onTick(millisUntilFinished: Long) {
        onActionTick(millisUntilFinished)
    }

    override fun onFinish() {
        onActionFinish()
    }
}