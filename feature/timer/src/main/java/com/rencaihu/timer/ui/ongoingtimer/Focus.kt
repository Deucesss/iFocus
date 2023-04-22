package com.rencaihu.timer.ui.ongoingtimer

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.rencaihu.timer.EXTRA_FOCUS

enum class STATUS {
    READY, RUNNING, PAUSED, COMPLETED
}

sealed class BaseFocus(
    val id: Long,
    val name: String,
    var status: STATUS,
    var progress: Int,
    val laps: Int,
    val lapDuration: Int, // in minutes
    var curLap: Int,
    // timestamp when onSaveInstanceState is called
    var timestampOnDestroy: Long? = null
) : Parcelable {

    abstract val timeRemaining: Int

    fun nextLap() {
        curLap = (curLap + 1).coerceAtMost(laps)
    }

    fun saveTimestamp() {
        if (status == STATUS.RUNNING) {
            timestampOnDestroy = System.currentTimeMillis()
        }
    }

    fun recalibrateTime() {
        if (timestampOnDestroy != null) {
            progress = (progress + ((System.currentTimeMillis() - timestampOnDestroy!!) / 1000).toInt()).coerceAtMost(curLap * lapDuration * 60)
            timestampOnDestroy = null
        }
    }

    override fun toString(): String =
        "BaseFocus(id=$id, name='$name', status=$status, progress=$progress, laps=$laps, lapDuration=$lapDuration, curLap=$curLap, timestampOnInvisible=$timestampOnDestroy)"
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeInt(status.ordinal)
        dest.writeInt(progress)
        dest.writeInt(laps)
        dest.writeInt(lapDuration)
        dest.writeInt(curLap)
        dest.writeValue(timestampOnDestroy)
    }
}

class DownFocus constructor(
    id: Long,
    name: String,
    status: STATUS,
    progress: Int,
    laps: Int,
    lapDuration: Int,
    curLap: Int = 0,
    timestampOnInvisible: Long? = null
) : BaseFocus(id, name, status, progress, laps, lapDuration, curLap, timestampOnInvisible), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        STATUS.values()[parcel.readInt()],
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override val timeRemaining: Int
        get() = curLap * lapDuration * 60 - progress

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
) : BaseFocus(id, name, status, progress, laps, lapDuration, 1, timestampOnInvisible) {

    override val timeRemaining: Int
        get() = TODO("Not yet implemented")

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