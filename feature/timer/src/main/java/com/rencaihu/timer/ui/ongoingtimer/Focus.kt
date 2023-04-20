package com.rencaihu.timer.ui.ongoingtimer

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@Parcelize
data class Focus(
    val id: Long,
    val name: String,
    val lapDuration: Int,
    val laps: Int,
    var status: STATUS,
    var progress: Int,
    private var timestampOnInvisible: Long? = null,
) : Parcelable {
    val totalDuration: Int get() = lapDuration * laps
    val currentLap: Int get() = progress / (lapDuration * 60)
    private val currentLapProgress: Int get() = progress % (lapDuration * 60)
    private val timeRemaining: Long get() = (lapDuration * 60 - currentLapProgress) * 1000L

    @IgnoredOnParcel
    private var timer: Timer? = null
    @IgnoredOnParcel
    private var onTick: ((millisUntilFuture: Long) -> Unit)? = null
    @IgnoredOnParcel
    private var onComplete: (() -> Unit)? = null

    fun start() {
        timer?.cancel()
        timer = Timer(
            timeRemaining,
            {
                if (status == STATUS.RUNNING) {
                    Timber.d("onTick: $this")
                    progress++
                    onTick!!(it)
                }
            },
            onComplete!!
        )
        timer!!.start()
    }

    fun pause() {
        timer?.cancel()
    }

    fun setOnTickListener(onTick: (millisInFuture: Long) -> Unit) {
        this.onTick = onTick
    }

    fun setOnCompleteListener(onComplete: () -> Unit) {
        this.onComplete = onComplete
    }

    fun recalibrateTimeOnStop() {
        if (status == STATUS.RUNNING) {
            timestampOnInvisible = System.currentTimeMillis()
        }
    }

    fun recalibrateTimeOnResume() {
        if (status == STATUS.RUNNING && timestampOnInvisible != null) {
            val timeElapsed = (System.currentTimeMillis() - timestampOnInvisible!!) / 1000
            progress += timeElapsed.toInt()
            timestampOnInvisible = null
        }
    }

    enum class STATUS {
        RUNNING, PAUSED, STOPPED, COMPLETED
    }

}