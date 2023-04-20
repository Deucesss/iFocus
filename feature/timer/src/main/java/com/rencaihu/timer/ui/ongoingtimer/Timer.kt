package com.rencaihu.timer.ui.ongoingtimer

import android.os.CountDownTimer


// TODO: write a new CountDownTimer that sends message after one second delay instead of immediately
class Timer(duration: Long): CountDownTimer(duration, 1000) {
    private var onActionTick: ((millisUntilFinished: Long) -> Unit)? = null
    private var onActionFinish: (() -> Unit)? = null

    fun setOnTickListener(onTick: (millisUntilFinished: Long) -> Unit) {
        onActionTick = onTick
    }

    fun setOnFinishListener(onFinish: () -> Unit) {
        onActionFinish = onFinish
    }

    override fun onTick(millisUntilFinished: Long) {
        onActionTick?.invoke(millisUntilFinished)
    }

    override fun onFinish() {
        onActionFinish?.invoke()
    }
}

