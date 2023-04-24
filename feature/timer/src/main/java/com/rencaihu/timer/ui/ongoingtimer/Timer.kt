package com.rencaihu.timer.ui.ongoingtimer

import android.os.Parcelable
import android.text.format.DateUtils
import kotlinx.parcelize.Parcelize


@Parcelize
class Timer internal constructor(
    val id: Int,
    val state: State,
    val durationPerLap: Long,
    val laps: Int,
    val lastLap: Int,
    val lastStartTime: Long,
    val lastRemainingTime: Long
) : Parcelable {

    @Parcelize
    sealed class State(
        /** The value assigned to this State in prior releases.  */
        val value: Int
    ) : Parcelable {

        companion object {
            /**
             * @return the state corresponding to the given `value`
             */
            fun fromValue(value: Int): State? {
                for (state in values()) {
                    if (state.value == value) {
                        return state
                    }
                }
                return null
            }

            fun values(): Array<State> {
                return arrayOf(READY, RUNNING, PAUSED, EXPIRED)
            }

            fun valueOf(value: String): State {
                return when (value) {
                    "READY" -> READY
                    "RUNNING" -> RUNNING
                    "PAUSED" -> PAUSED
                    "EXPIRED" -> EXPIRED
                    else -> throw IllegalArgumentException("No object com.android.deskclock.data.Timer.State.$value")
                }
            }
        }
        object READY: State(0)
        object RUNNING : State(1)
        object PAUSED : State(2)
        object EXPIRED : State(3)
    }

    val isReady: Boolean
        get() = state == State.READY

    val isRunning: Boolean
        get() = state == State.RUNNING

    val isPaused: Boolean
        get() = state == State.PAUSED

    val isExpired: Boolean
        get() = state == State.EXPIRED

    val remainingTime: Long
        get() {
            if (state == State.PAUSED) {
                return lastRemainingTime
            }
            val now = System.currentTimeMillis()
            val elapsed = maxOf(0, now - lastStartTime)
            return (lastRemainingTime - elapsed).coerceAtLeast(0)
        }

    val expirationTime: Long
        get() {
            check(!(state == State.RUNNING || state == State.EXPIRED)) {
                "Cannot compute expiration time in state $state"
            }
            return lastStartTime + lastRemainingTime
        }

    val elapsedTime: Long
        get() = lastLap * durationPerLap - remainingTime

    val lapsRemaining: Int
        get() = laps - lastLap + 1

    fun start(): Timer {
        if (state == State.RUNNING || state == State.EXPIRED) {
            return this
        }

        return Timer(
            id,
            State.RUNNING,
            durationPerLap,
            laps,
            lastLap,
            System.currentTimeMillis(),
            lastRemainingTime
        )
    }

    fun pause(): Timer {
        if (state == State.PAUSED) {
            return this
        }

        val remainingTime = this.remainingTime
        return Timer(
            id,
            State.PAUSED,
            durationPerLap,
            laps,
            lastLap,
            UNUSED,
            remainingTime
        )
    }

    fun nextLap(): Timer {
        return Timer(
            id,
            State.RUNNING,
            durationPerLap,
            laps,
            (lastLap + 1).coerceAtMost(laps),
            System.currentTimeMillis(),
            durationPerLap
        )
    }

    fun expire(): Timer {
        if (state == State.EXPIRED) {
            return this
        }

        val remainingTime = java.lang.Long.min(0, lastRemainingTime)
        return Timer(id, State.EXPIRED, durationPerLap, laps, lastLap, UNUSED, remainingTime)
    }

    fun setDurationPerLap(durationPerLap: Long): Timer =
        Timer(
            id,
            State.READY,
            durationPerLap,
            laps,
            1,
            UNUSED,
            durationPerLap
        )

    fun setLaps(laps: Int): Timer =
        Timer(
            id,
            State.READY,
            durationPerLap,
            laps,
            1,
            UNUSED,
            durationPerLap
        )

    companion object {
        /** The minimum duration of a timer.  */
        @JvmField
        val MIN_LENGTH: Long = DateUtils.SECOND_IN_MILLIS

        /** The maximum duration of a new timer created via the user interface.  */
        val MAX_LENGTH: Long = 99 * DateUtils.HOUR_IN_MILLIS + 99 * DateUtils.MINUTE_IN_MILLIS + 99 * DateUtils.SECOND_IN_MILLIS

        const val UNUSED = Long.MIN_VALUE

        @JvmStatic
        fun newTimer(id: Int, durationPerLap: Long, laps: Int) =
            Timer(id, State.READY, durationPerLap, laps, 1, UNUSED, durationPerLap)
    }
}