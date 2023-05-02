package com.rencaihu.timer.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.rencaihu.ifocus.proto.BreakProto
import com.rencaihu.ifocus.proto.TimerProto
import com.rencaihu.timer.ui.ongoingtimer.Timer
import java.io.InputStream
import java.io.OutputStream

val Context.timerStore: DataStore<TimerProto> by dataStore(
    fileName = "timer.pb",
    serializer = TimerSerializer
)

val Context.breakTimerStore: DataStore<BreakProto> by dataStore(
    fileName = "break.pb",
    serializer = BreakTimerSerializer
)

fun TimerProto.asDomainModel() =
    Timer(
        id = id,
        state = Timer.State.fromValue(state)!!,
        durationPerLap = durationPerLap,
        laps = laps,
        lastLap = lastLap,
        lastStartTime = lastStartTime,
        lastRemainingTime = lastRemainingTime,
        isBreakTimer = false
    )

fun BreakProto.asDomainModel() =
    Timer(
        id = id,
        state = Timer.State.fromValue(state)!!,
        durationPerLap = durationPerLap,
        laps = laps,
        lastLap = lastLap,
        lastStartTime = lastStartTime,
        lastRemainingTime = lastRemainingTime,
        isBreakTimer = true
    )


object BreakTimerSerializer : Serializer<BreakProto> {
    override val defaultValue: BreakProto
        get() = BreakProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): BreakProto {
        try {
            return BreakProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: BreakProto, output: OutputStream) =
        t.writeTo(output)
}

object TimerSerializer : Serializer<TimerProto> {
    override val defaultValue: TimerProto
        get() = TimerProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TimerProto {
        try {
            return TimerProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: TimerProto, output: OutputStream) =
        t.writeTo(output)
}