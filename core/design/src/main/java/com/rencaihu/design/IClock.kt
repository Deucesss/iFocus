package com.rencaihu.design

interface IClock {
    fun setProgress(progress: Int)
    fun setLaps(laps: Int)
    fun setLapDuration(minutes: Int)

    fun setCurrentLap(curLap: Int)
}