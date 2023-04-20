package com.rencaihu.timer.ui.ongoingtimer

import android.os.Parcel
import android.os.Parcelable
import timber.log.Timber

enum class STATUS {
    CREATED, RUNNING, PAUSED, STOPPED, COMPLETED
}

abstract class BaseFocus(
    val id: Long,
    val name: String,
    var status: STATUS,
    var progress: Int,
    var timestampOnInvisible: Long? = null
) {
    var timer: Timer? = null

    private var onTickListener: (Long) -> Unit = {}
    private var onFinishListener: () -> Unit = {}

    abstract fun getDuration(): Long

    fun setOnTickListener(onTick: (millisUntilFuture: Long) -> Unit) {
        this.onTickListener = onTick
    }

    fun setOnFinishListener(onComplete: () -> Unit) {
        this.onFinishListener = onComplete
    }

    fun recalibrateProgressAndResume() {
        if (status == STATUS.RUNNING && timestampOnInvisible != null) {
            val timeElapsed = System.currentTimeMillis() - timestampOnInvisible!!
            val progress = (timeElapsed / 1000).toInt()
            Timber.d("timeElapse: $timeElapsed, recalibrateProgress: $progress, progress: ${this.progress}")
            this.progress += progress
            timer = newTimer()
            timer!!.start()
        }
    }

    fun recordTimeUponBackground() {
        if (status == STATUS.RUNNING) {
            timer?.cancel()
            timestampOnInvisible = System.currentTimeMillis()
        }
    }

    fun start() {
        if (status == STATUS.CREATED) {
            status = STATUS.RUNNING
            timer = newTimer()
            timer!!.start()
        }
    }

    fun pause() {
        if (status == STATUS.RUNNING) {
            status = STATUS.PAUSED
            timer?.cancel()
        }
    }

    fun resume() {
        if (status != STATUS.RUNNING) {
            status = STATUS.RUNNING
            timer = newTimer()
            timer!!.start()
        }
    }

    fun stop() {
        if (status != STATUS.STOPPED) {
            status = STATUS.STOPPED
            timer?.cancel()
        }
    }

    private fun newTimer(): Timer =
        Timer(getDuration()).apply {
            timer?.cancel()
            setOnTickListener(onTickListener)
            setOnFinishListener(onFinishListener)
        }
}

class DownFocus constructor(
    id: Long,
    name: String,
    status: STATUS,
    progress: Int,
    val lapDuration: Int,
    val laps: Int,
    timestampOnInvisible: Long? = null
) : BaseFocus(id, name, status, progress, timestampOnInvisible), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        STATUS.values()[parcel.readInt()],
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeInt(status.ordinal)
        dest.writeInt(progress)
        dest.writeInt(lapDuration)
        dest.writeInt(laps)
        dest.writeValue(timestampOnInvisible)
    }

    private val timeRemaining: Long
        get() {
            val currentLapProgress = progress % (lapDuration * 60)
            return (lapDuration * 60 - currentLapProgress) * 1000L
        }

    override fun getDuration(): Long = timeRemaining

    companion object CREATOR : Parcelable.Creator<DownFocus> {
        override fun createFromParcel(parcel: Parcel): DownFocus {
            return DownFocus(parcel)
        }

        override fun newArray(size: Int): Array<DownFocus?> {
            return arrayOfNulls(size)
        }

        @JvmStatic
        fun newInstance(id: Long, name: String, lapDuration: Int, laps: Int) = DownFocus(
            id = id,
            name = name,
            status = STATUS.CREATED,
            progress = 0,
            lapDuration = lapDuration,
            laps = laps
        )
    }
}

class UpFocus(
    id: Long,
    name: String,
    status: STATUS,
    progress: Int
) : BaseFocus(id, name, status, progress), Parcelable {
    override fun getDuration(): Long = Long.MAX_VALUE

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        STATUS.values()[parcel.readInt()],
        parcel.readInt()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeInt(status.ordinal)
        dest.writeInt(progress)
    }

    companion object CREATOR : Parcelable.Creator<UpFocus> {
        @JvmStatic
        fun newInstance(id: Long, name: String) = UpFocus(
            id = id,
            name = name,
            status = STATUS.CREATED,
            progress = 0
        )

        override fun createFromParcel(parcel: Parcel): UpFocus {
            return UpFocus(parcel)
        }

        override fun newArray(size: Int): Array<UpFocus?> {
            return arrayOfNulls(size)
        }

    }
}

//@Parcelize
//data class Focus(
//    val id: Long,
//    val name: String,
//    val lapDuration: Int,
//    val laps: Int,
//    var status: STATUS,
//    var progress: Int,
//    private var timestampOnInvisible: Long? = null,
//) : Parcelable {
//    val totalDuration: Int get() = lapDuration * laps
//    val currentLap: Int get() = progress / (lapDuration * 60)
//    private val currentLapProgress: Int get() = progress % (lapDuration * 60)
//    private val timeRemaining: Long get() = (lapDuration * 60 - currentLapProgress) * 1000L
//
//    @IgnoredOnParcel
//    private var timer: Timer? = null
//    @IgnoredOnParcel
//    private var onTick: ((millisUntilFuture: Long) -> Unit)? = null
//    @IgnoredOnParcel
//    private var onComplete: (() -> Unit)? = null
//
//    fun start() {
//        timer?.cancel()
//        timer = Timer(
//            timeRemaining,
//            {
//                if (status == STATUS.RUNNING) {
//                    Timber.d("onTick: $this")
//                    progress++
//                    onTick!!(it)
//                }
//            },
//            onComplete!!
//        )
//        timer!!.start()
//    }
//
//    fun pause() {
//        timer?.cancel()
//    }
//
//    fun setOnTickListener(onTick: (millisInFuture: Long) -> Unit) {
//        this.onTick = onTick
//    }
//
//    fun setOnCompleteListener(onComplete: () -> Unit) {
//        this.onComplete = onComplete
//    }
//
//    fun recalibrateTimeOnStop() {
//        if (status == STATUS.RUNNING) {
//            timestampOnInvisible = System.currentTimeMillis()
//        }
//    }
//
//    fun recalibrateTimeOnResume() {
//        if (status == STATUS.RUNNING && timestampOnInvisible != null) {
//            val timeElapsed = (System.currentTimeMillis() - timestampOnInvisible!!) / 1000
//            progress += timeElapsed.toInt()
//            timestampOnInvisible = null
//        }
//    }

//}