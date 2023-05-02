package com.rencaihu.timer.data

import android.content.Context
import com.rencaihu.timer.datastore.asDomainModel
import com.rencaihu.timer.datastore.breakTimerStore
import com.rencaihu.timer.datastore.timerStore
import com.rencaihu.timer.ui.ongoingtimer.Timer
import kotlinx.coroutines.flow.firstOrNull

class TimerDAO(private val context: Context) {

    suspend fun readTimer(): Timer? {
        val breakTimer = context.breakTimerStore.data.firstOrNull()
        if (breakTimer?.id != 0) {
            return breakTimer?.asDomainModel()
        }
        val timer = context.timerStore.data.firstOrNull()
        if (timer?.id != 0) {
            return timer?.asDomainModel()
        }
        return null
    }

    suspend fun saveTimer(timer: Timer) {
        if (timer.isBreakTimer) {
            context.breakTimerStore.updateData { breakProto ->
                breakProto.toBuilder()
                    .setId(timer.id)
                    .setState(timer.state.value)
                    .setLaps(timer.laps)
                    .setDurationPerLap(timer.durationPerLap)
                    .setLastLap(timer.lastLap)
                    .setLastStartTime(timer.lastStartTime)
                    .setLastRemainingTime(timer.lastRemainingTime)
                    .build()
            }
        } else {
            context.timerStore.updateData { timerProto ->
                timerProto.toBuilder()
                    .setId(timer.id)
                    .setState(timer.state.value)
                    .setLaps(timer.laps)
                    .setDurationPerLap(timer.durationPerLap)
                    .setLastLap(timer.lastLap)
                    .setLastStartTime(timer.lastStartTime)
                    .setLastRemainingTime(timer.lastRemainingTime)
                    .build()
            }
        }
    }

    suspend fun deleteTimer(timer: Timer) {
        if (timer.isBreakTimer) {
            context.breakTimerStore.updateData { breakProto ->
                breakProto.toBuilder().clear().build()
            }
        } else {
            context.timerStore.updateData { timerProto ->
                timerProto.toBuilder().clear().build()
            }
        }
    }
}