package com.rencaihu.timer.ui.ongoingtimer

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.rencaihu.timer.EXTRA_FOCUS
import timber.log.Timber

enum class STATUS {
    READY, RUNNING, PAUSED, STOPPED, COMPLETED
}

sealed class BaseFocus(
    val id: Long,
    val name: String,
    var status: STATUS,
    var progress: Int,
    val laps: Int,
    val lapDuration: Int,
    var timestampOnInvisible: Long? = null
) : Parcelable {
    var timer: Timer? = null

    private var onTickListener: (Long) -> Unit = {}
    private var onFinishListener: () -> Unit = {}

    /**
     * @return duration in milliseconds calculated by child class
     */
    abstract fun getDuration(): Long

    abstract val isCompleted: Boolean

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
        if (status == STATUS.READY) {
            status = STATUS.RUNNING
            timer = newTimer()
            timer!!.start()
            return
        }

        if (status == STATUS.COMPLETED) {
            onFinishListener()
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

    override fun toString(): String =
        "BaseFocus(id=$id, name='$name', status=$status, progress=$progress, laps=$laps, lapDuration=$lapDuration, timestampOnInvisible=$timestampOnInvisible)"

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeInt(status.ordinal)
        dest.writeInt(progress)
        dest.writeInt(laps)
        dest.writeInt(lapDuration)
        dest.writeValue(timestampOnInvisible)
    }
}

class DownFocus constructor(
    id: Long,
    name: String,
    status: STATUS,
    progress: Int,
    laps: Int,
    lapDuration: Int,
    timestampOnInvisible: Long? = null
) : BaseFocus(id, name, status, progress, laps, lapDuration, timestampOnInvisible), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        STATUS.values()[parcel.readInt()],
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override val isCompleted: Boolean
        get() {
            val isCompleted = progress >= lapDuration * 60 * laps
            if (isCompleted) {
                status = STATUS.COMPLETED
            }
            return isCompleted
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

        fun newBundle(id: Long, name: String, lapDuration: Int, laps: Int) =
            bundleOf(EXTRA_FOCUS to newInstance(id, name, lapDuration, laps))

        @JvmStatic
        fun newInstance(id: Long, name: String, lapDuration: Int, laps: Int) = DownFocus(
            id = id,
            name = name,
            status = STATUS.READY,
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
    progress: Int,
    laps: Int = 1,
    lapDuration: Int = Int.MAX_VALUE,
    timestampOnInvisible: Long? = null
) : BaseFocus(id, name, status, progress, laps, lapDuration, timestampOnInvisible) {
    override fun getDuration(): Long = Long.MAX_VALUE

    override val isCompleted: Boolean
        get() = false

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        STATUS.values()[parcel.readInt()],
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    companion object CREATOR : Parcelable.Creator<UpFocus> {
        @JvmStatic
        fun newInstance(id: Long, name: String) = UpFocus(
            id = id,
            name = name,
            status = STATUS.READY,
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