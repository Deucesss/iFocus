package com.rencaihu.common.ext

object TimeUtil {

    fun hoursInSeconds(timeInSeconds: Int) =
        timeInSeconds / 3600

    fun minuteInSeconds(timeInSeconds: Int) =
        (timeInSeconds % 3600) / 60

    fun secondsInSeconds(timeInSeconds: Int) =
        timeInSeconds % 3600 % 60

    fun formatTimeInSeconds(timeInSeconds: Int) =
        String.format(
            "%02d:%02d:%02d",
            hoursInSeconds(timeInSeconds),
            minuteInSeconds(timeInSeconds),
            secondsInSeconds(timeInSeconds)
        )

    fun hoursFromMillis(timeInMillis: Long) =
        ((timeInMillis / 1000) / 3600).toInt()

    fun minutesInMillis(timeInMillis: Long) =
        (((timeInMillis / 1000) % 3600) / 60).toInt()

    fun secondsInMillis(timeInMillis: Long) =
        ((timeInMillis / 1000) % 3600 % 60).toInt()
}