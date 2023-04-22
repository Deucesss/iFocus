package com.rencaihu.common

import android.os.Handler
import android.os.Looper
import android.os.Message


abstract class MyCountDownTimer(
    private val millisInFuture: Long,
    private val interval: Long
) {

    private var mCancelled = false
    private var lastTickMillis = 0L
    private var stopTimeInFuture = 0L

    @Synchronized
    fun start(): MyCountDownTimer {

        if (millisInFuture <= 0) {
            onFinish()
            return this
        }
        mCancelled = false
        val msg = mHandler.obtainMessage(MSG)
        stopTimeInFuture = System.currentTimeMillis() + millisInFuture
        mHandler.sendMessageDelayed(msg, interval)
        return this
    }

    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    abstract fun onTick(millisUntilFinished: Long)

    abstract fun onFinish()

    private val mHandler: Handler =
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                if (mCancelled) return

                val millisLeft = stopTimeInFuture - System.currentTimeMillis()

                if (millisLeft <= 0) {
                    onFinish()
                } else {
                    onTick(millisLeft)
                    val now = System.currentTimeMillis()
                    var delay = interval

                    if (lastTickMillis != 0L) {
                        delay = interval - (now - lastTickMillis)
                        lastTickMillis += 1000L
                    } else {
                        lastTickMillis = now + 1000L
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay)
                }

            }
        }

    companion object {
        const val MSG = 1
    }

}